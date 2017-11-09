package com.minivision.cameraplat.mqtt.task;

import com.minivision.cameraplat.domain.FaceSet;
import com.minivision.cameraplat.util.CommonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
public class ExImportTask {

    @Autowired
    private SimpMessagingTemplate messageTemplate;

    @Value("${system.store.people}")
    private String filepath;

    @Value("${backup.mysqlPath}")
    private String mysqlDir;

    @Value("${backup.redisPath}")
    private String redisPath;

    @Value("${backup.rarPath}")
    private String rarPath;

    private ExecutorService executorService;

    public static final int RUNNING = 1;

    public static final int DONE = 2;

    public static final int FAILED = 3;

    private volatile int status;

    private volatile int exportStatus;

    @Autowired
    private BatchTaskContext taskContext;

    private volatile boolean continued = true;

    @PostConstruct
    private void init() {
        executorService = Executors.newFixedThreadPool(3);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExportStatus() {
        return exportStatus;
    }

    public void setExportStatus(int exportStatus) {
        this.exportStatus = exportStatus;
    }

    public String export() {
        try {
            long startTime = System.nanoTime();
            File file = new File(filepath);
            File imfile = new File(filepath + File.separator + "import.zip");
            File exfile = new File(filepath + File.separator + "export.zip");
            if (imfile.exists()) {
                FileUtils.deleteQuietly(imfile);
            }
            if (exfile.exists()) {
                FileUtils.deleteQuietly(exfile);
            }
            this.setExportStatus(RUNNING);
            //mysql
            this.sendLog("get ready to export data from database......");
            String mysql =
                    "cmd /c " + mysqlDir + " -u root -proot --opt --default-character-set=utf8 camera > "
                            + filepath + File.separator + "camera.sql";
            Process process = Runtime.getRuntime().exec(mysql);
            if (process.waitFor() != 0) {
                this.sendLog("failed to export data from database......");
                return "failed";
            }
            //redis
            this.sendLog("get ready to export data from redis......");
            //File mysqlDir = new File(webPath);
            File redisFile = new File(redisPath + File.separator + "dump.rdb");
            FileUtils.copyFileToDirectory(redisFile, file, false);
            this.sendLog("export redis data success,get ready to compression......");
//            File desFile = new File(filepath + File.separator+"export.zip");
//            if(desFile.exists()){
//                FileUtils.deleteQuietly(desFile);
//            }
//            Project prj = new Project();
//            Zip zip = new Zip();
//            zip.setProject(prj);
//            zip.setDestFile(desFile);
//            FileSet fileSet = new FileSet();
//            fileSet.setProject(prj);
//            fileSet.setDir(file);
//            zip.addFileset(fileSet);
//            zip.execute();
            String zip = rarPath + " a -r -ep1 -ibck " + filepath + File.separator + "export.zip " + filepath + File.separator;
            process = Runtime.getRuntime().exec(zip);
            if (process.waitFor() != 0) {
                System.out.println("compression failed");
            } else {
                this.sendLog("compression success......");
            }
            long endTime = System.nanoTime();
            this.sendLog("compression success,cost " + getCostTime(endTime - startTime));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "failed";
        } finally {
            this.setExportStatus(DONE);
            Map<String, Integer> status = new HashMap<>();
            status.put("status", DONE);
            this.sendExportStatus(status);
        }
        return "success";
    }

    public String importTask(File file)

    {
        try {
            long startTime = System.nanoTime();
            this.setStatus(RUNNING);
            this.sendLog("start to compress......");
//            Project proj = new Project();
//            Expand expand = new Expand();
//            expand.setProject(proj);
//            expand.setTaskType("unzip");
//            expand.setTaskName("unzip");
//            expand.setSrc(file);
//            expand.setOverwrite(true);
//            expand.setDest(new File(filepath));
//            // expand.setEncoding(ENCODE);
//            expand.execute();
            if (!file.exists())
                throw new RuntimeException("zip file " + " does not exist.");
            File desFile = new File(filepath);
            if (!desFile.exists() || !desFile.isDirectory()) {
                desFile.mkdirs();
            }
            String oriPath = file.getAbsolutePath();
            String unzip = rarPath + "  x -y -ibck " + oriPath + " " + filepath + File.separator;
            Process process = Runtime.getRuntime().exec(unzip);
            if (process.waitFor() != 0) {
                this.sendLog("decompression failed......");
            } else {
                this.sendLog("decompression success......");
            }
            this.sendLog("decompression success，get ready to import data......");
            boolean result = importExecuter();
            File sqlFile = new File(filepath + File.separator + "camera.sql");
            File redisFile = new File(filepath + File.separator + "dump.rdb");
            //File zipFile = new File(filepath + File.separator+"export.zip");
            FileUtils.deleteQuietly(sqlFile);
            FileUtils.deleteQuietly(redisFile);
            if (result) {
                String redisCmd = "cmd /c net stop redis";
                process = null;
                try {
                    process = Runtime.getRuntime().exec(redisCmd);
                    if (process.waitFor() == 0) {
                        this.sendLog("redis restarting...");
                        redisCmd =
                                "cmd /c net start redis";
                        process = Runtime.getRuntime().exec(redisCmd);
                        if (process.waitFor() == 0) {
                            this.sendLog("redis restart success......");
                        }
                        this.sendLog("import success , cost " + getCostTime(System.nanoTime() - startTime) + "......");
                    }
                } catch (Exception e) {
                    this.sendLog("redis restart failed...");
                }
                return "success";
            } else {
                this.sendLog("failed to import...");
                return "false";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.sendLog("failed to import..." + e.getMessage());
            return "false";
        } catch (IOException | InterruptedException e) {
            this.sendLog("failed to decompression..." + e.getMessage());
            return "false";
        } finally {
            Map<String, Integer> status = new HashMap<>();
            status.put("status", DONE);
            this.sendStatus(status);
            this.setStatus(DONE);
        }
    }

    public boolean importExecuter() {
        Boolean result = false;
        try {
            CountDownLatch latch = new CountDownLatch(2);
            Future<Boolean> sql = executorService.submit(new SqlTask(latch));
//            Future<Boolean> img = executorService.submit(new ImgTask(latch));
            Future<Boolean> redis = executorService.submit(new RedisTask(latch));
            latch.await();
            result = sql.get() && redis.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    class SqlTask implements Callable<Boolean> {
        private CountDownLatch countDownLatch;

        public SqlTask(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Boolean call() {
            try {
                if (!new File(filepath + File.separator + "camera.sql").exists()) {
                    sendLog("database failed to import,database file not existed !");
                    return false;
                }
                sendLog("start to import database data......");
                SQLExec sqlExec = new SQLExec();
                //设置数据库参数
                sqlExec.setDriver("com.mysql.jdbc.Driver");
                sqlExec.setUrl("jdbc:mysql://127.0.0.1:3306/camera");
                sqlExec.setUserid("root");
                sqlExec.setPassword("root");
                //要执行的脚本
                sqlExec.setSrc(new File(filepath + File.separator + "camera.sql"));
                //有出错的语句该如何处理
                sqlExec.setOnerror((SQLExec.OnError) (EnumeratedAttribute
                        .getInstance(SQLExec.OnError.class, "abort")));
                sqlExec.setPrint(true); //设置是否输出
                //输出到文件 sql.out 中；不设置该属性，默认输出到控制台
                sqlExec.setOutput(new File(filepath + File.separator + "sql.out"));
                sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错
                sqlExec.setEncoding("UTF-8");
                sqlExec.execute();
                sendLog("import database data success......");
            } catch (Exception e) {
                sendLog("database import error......." + e.getMessage());
                return false;
            } finally {
                countDownLatch.countDown();
            }
            return true;
        }
    }

    class RedisTask implements Callable<Boolean> {

        private CountDownLatch countDownLatch;

        public RedisTask(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Boolean call() {
            try {
                sendLog("importing redis data .......");
                File srcRedis = new File(filepath + File.separator + "dump.rdb");
                FileUtils.copyFileToDirectory(srcRedis, new File(redisPath), true);
                sendLog("import redis data success .......");
            } catch (IOException e) {
                sendLog("fail to import redis data ........" + e.getMessage());
                return false;
            } finally {
                countDownLatch.countDown();
            }
            return true;
        }
    }

    @Async
    public void Unzip(File batchFile, File fileDir ,FaceSet faceSet,String userName) {
        try {
            this.sendRegistryLog("Unzipping...");
            String imgPath = "";
            String oriPath = batchFile.getAbsolutePath();
            String unzip = rarPath + "  x -y -ibck " + oriPath + " " + fileDir.getAbsolutePath() + File.separator;
            this.continued = true;
            new Thread(() -> {
                long oriSize = getZipTrueSize(batchFile);
//                    System.out.println("原始大小..."+oriSize);
                while (continued) {
                    long size = FileUtils.sizeOf(fileDir);
                    try {
                        double percent = (double) size/oriSize*100;
                        String formatPercent = new DecimalFormat("#.00").format(percent)+"%";
                        Map<String, Object> progress = new HashMap<>();
                        progress.put("percent", formatPercent);
                        sendRegistryStatus(progress);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continued = false;
                    }
                }
            }).start();
            Process process = Runtime.getRuntime().exec(unzip);
            if (process.waitFor() != 0) {
                Map<String,Object> status = new HashMap();
                status.put("status",FAILED);
                sendRegistryStatus(status);
                sendRegistryLog("The decompression failed. Please verify the location of the compression tool is correct...");
            }
            else{
                this.continued = false;
                Map<String, Object> progress = new HashMap<>();
                progress.put("percent", 100);
                sendRegistryStatus(progress);
                //return;
            }
            FileUtils.deleteQuietly(batchFile);
            this.sendRegistryLog("Unzip successed...");
            List<String> paths = CommonUtils.listDir(fileDir.getPath());
            if (null != paths && paths.size() > 0) {
                imgPath = paths.get(0);
            }
            Map<String,Object> status = new HashMap();
            status.put("status",DONE);
            this.sendRegistryStatus(status);
            BatchRegistTask task = taskContext
                    .createBatchRegistTask(faceSet, userName, new File(imgPath));
            taskContext.submitTask(task);
        } catch (Exception e) {
            this.continued = false;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Map<String,Object> status = new HashMap();
            status.put("status",FAILED);
            sendRegistryStatus(status);
            if(e instanceof IOException)
                sendRegistryLog("The decompression failed. Please verify the location of the compression tool is correct...");
            else{
                sendRegistryLog("The decompression failed...."+e);
            }
        }
    }

    public void sendLog(String log) {
        messageTemplate.convertAndSend("/i/tasklog/", log);
    }
    public void sendRegistryLog(String log) {
        messageTemplate.convertAndSend("/i/registry/", log);
    }
    public void sendRegistryStatus(Map<String, Object> status) {
        messageTemplate.convertAndSend("/i/registryStatus/", status);
    }

    public void sendStatus(Map<String, Integer> status) {
        messageTemplate.convertAndSend("/i/taskStatus/", status);
    }

    public void sendExportStatus(Map<String, Integer> status) {
        messageTemplate.convertAndSend("/i/taskExportStatus/", status);
    }

    String getCostTime(long costTime) {

        long between = TimeUnit.NANOSECONDS.toSeconds(costTime);
        long day = between / (24 * 60 * 60);
        long hour = (between / (60 * 60) - day * 24);
        long min = ((between / (60)) - day * 24 * 60 - hour * 60);
        long s = (between - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return "" + day + "days" + hour + "hours" + min + "minutes" + s + "seconds";
    }

    public static long getZipTrueSize(File file) {
        long size = 0;
        ZipFile f;
        try {
            f = new ZipFile(file.getPath());
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }
}

package com.minivision.camaraplat;


import com.google.common.collect.Lists;
import com.minivision.camaraplat.task.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=FileTest.class)
public class FileTest {


    private long lastSize;

    private static volatile boolean  isok = true;

    @Test
    public void test(){
        try {
            Project proj = new Project();
            Expand expand = new Expand();
            expand.setProject(proj);
            expand.setTaskType("unzip");
            expand.setTaskName("unzip");
            expand.setSrc(new File("E:\\camera\\picture_store\\people\\export.zip"));
            expand.setOverwrite(true);
            expand.setDest(new File("D://test"));
            // expand.setEncoding(ENCODE);
            expand.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void zip(){
        try {
            File desFile = new File("E:\\camera\\picture_store\\people/export.zip");
            if(desFile.exists()){
                FileUtils.deleteQuietly(desFile);
            }
            Project prj = new Project();
            Zip zip = new Zip();
            zip.setProject(prj);
            zip.setDestFile(desFile);
            FileSet fileSet = new FileSet();
            fileSet.setProject(prj);
            fileSet.setDir(new File("E:\\camera\\picture_store\\people"));
            zip.addFileset(fileSet);
            zip.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void newTest(){
        String srcPathName1 = "E:\\camera\\picture_store\\people";
        String zipPath = "E:\\camera\\picture_store\\people/export.zip";
        ZipUtil.compress(zipPath,srcPathName1);
    }

    @Test
     public void Test2() {
       //  String mysql =" C:\\Program Files (x86)\\WinRAR\\winrar.exe a E:\\camera\\picture_store\\people/export.zip people" ;
         String mysql =" C:\\Program Files (x86)\\WinRAR\\winrar.exe a -r -ep1 -ibck E:\\logs\\test/export.zip E:\\logs\\test\\" ;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(mysql);
            if (process.waitFor() != 0) {
                System.out.println("failed");
            }
            else {
                System.out.println("ok");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unzip() {
//       String mysql =" C:\\Program Files (x86)\\WinRAR\\winrar.exe x -y -ibck  D:\\test\\export.zip E:\\camera\\picture_store\\people\\" ;
//       // String mysql =" C:\\Program Files (x86)\\WinRAR\\winrar.exe e -y E:\\logs\\test\\export.zip E:\\logs\\test\\" ;
//        Process process = null;
//        try {
//            new Thread(new Runnable() {
//                private boolean test = true;
//                @Override
//                public void run() {
//                    long oriSize = getZipTrueSize("D:\\test\\export.zip");
//                    System.out.println("原始大小"+oriSize);
//                    while (test) {
//                        long size = FileUtils.sizeOf(new File("E:\\camera\\picture_store\\people"));
//                        try {
//                            System.out.println(size);
////                            if(size == lastSize){
////                                test = false;
////                                System.out.println("已经解压缩完毕");
////                                break;
////                            }
//                           // lastSize = size;
//                            Thread.sleep(1000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
//            process = Runtime.getRuntime().exec(mysql);
//            if (process.waitFor() != 0) {
//                System.out.println("failed");
//            }
//            else {
//                System.out.println("ok");
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void test2(){
       //System.out.println(FileUtils.sizeOf(new File("D:\\mysql\\mysql.rar")));
        System.out.println(getZipTrueSize("D:\\test\\export.zip"));
        System.out.println(FileUtils.sizeOf(new File("D:\\test\\export.zip")));

    }
    public static long getZipTrueSize(String filePath) {
        long size = 0;
        ZipFile f;
        try {
            f = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

//    public static void main(String[] args) {
//        String mysql =" C:\\Program Files (x86)\\WinRAR\\winrar.exe x -y -ibck  D:\\test\\test.zip E:\\camera\\picture_store\\people\\" ;
//        Process process = null;
//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    long oriSize = getZipTrueSize("D:\\test\\test.zip");
//                    System.out.println("原始大小"+oriSize);
//                    while (isok) {
//                        long size = FileUtils.sizeOf(new File("E:\\camera\\picture_store\\people"));
//                        try {
//                            double percent = (double) size/oriSize*100;
//                            System.out.println(new DecimalFormat("#.00").format(percent)+"%");
////                            if(size == lastSize){
////                                test = false;
////                                break;
////                            }
//                            // lastSize = size;
//                            Thread.sleep(1000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                      System.out.println("已经解压缩完毕");
//                }
//            }).start();
//            process = Runtime.getRuntime().exec(mysql);
//            if (process.waitFor() != 0) {
//                System.out.println("failed");
//            }
//            else {
//                isok = false;
//                System.out.println("ok");
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    @Test
    public void Op(){
        File file = null;
        System.out.println(Optional.ofNullable(file).orElse(null));
    }

    @Test
    public void double2(){
        TemFaceResult temFaceResult= new TemFaceResult(0.01,"","");
        TemFaceResult temFaceResult2= new TemFaceResult(0.22,"","");
        TemFaceResult temFaceResult3= new TemFaceResult(0.23,"","");
//        TemFaceResult temFaceResult4= new TemFaceResult(0.32,"","");
        List<TemFaceResult> list = Lists.newArrayList();
//        list.add(temFaceResult);
//        list.add(temFaceResult3);
//        list.add(temFaceResult2);
        double t = list.stream().max(Comparator.comparingDouble(o -> o.confidence)).map(
            TemFaceResult::getConfidence).orElse(0.00d);
        System.out.println(t);
    }

    class TemFaceResult{

        private double confidence;

        private String faceSetToken;

        private String faceToken;

        public TemFaceResult(double confidence, String faceSetToken, String faceToken) {
            this.confidence = confidence;
            this.faceSetToken = faceSetToken;
            this.faceToken = faceToken;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getFaceSetToken() {
            return faceSetToken;
        }

        public void setFaceSetToken(String faceSetToken) {
            this.faceSetToken = faceSetToken;
        }

        public String getFaceToken() {
            return faceToken;
        }

        public void setFaceToken(String faceToken) {
            this.faceToken = faceToken;
        }
    }

    @Test
    public void timeTest(){
       // System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:MM:SS")));
       // String a = "1";
       // System.out.println((Long.valueOf(a).equals(1L)));
//        System.out.println(LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.DAYS));
        LocalDate now = LocalDate.now().minusDays(2);
        System.out.println(LocalDate.of(2015, 12, 25).isBefore(now));

    }
    private List<String> fileList(File file,List<String> resultFileName){
        File[] files = file.listFiles();
        if(files==null)return resultFileName;// 判断目录下是不是空的
        for (File f : files) {
            if (f.isDirectory()) {// 判断是否文件夹
                resultFileName.add(f.getPath());
                fileList(f, resultFileName);// 调用自身,查找子目录
            }
        }
        return resultFileName;
    }

    @Test
    public void stopTest() throws InterruptedException {
        StopWatch stopWatch = new StopWatch("test");
        stopWatch.start("A");
        TimeUnit.SECONDS.sleep(5);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    public static void main(String[] args) {
        System.out.println( (long) 21 << 30 | 16);
    }
}


package com.minivision.cameraplat.mqtt.task;

import com.google.common.collect.Lists;
import com.minivision.cameraplat.domain.Camera;
import com.minivision.cameraplat.repository.MonitorRecordRepository;
import com.minivision.cameraplat.repository.SnapshotRecordRepository;
import com.minivision.cameraplat.service.CameraService;
import com.minivision.cameraplat.service.MonitorRecordService;
import com.minivision.cameraplat.service.SnapShotRecordService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Component
public class BatchRecordDeleteTask {

    private static final Logger logger = LoggerFactory.getLogger(BatchRecordDeleteTask.class);

    @Autowired
    private MonitorRecordService monitorRecordService;

    @Autowired
    private SnapShotRecordService snapShotRecordService;

    @Autowired
    private CameraService cameraService;

    @Value("${system.store.snapshot}")
    private String snapPath;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    @Scheduled(cron = "${system.fileSchedule: 00 00 03 * * ? }")
    public void fileClean() {
        List<Camera> cameras = cameraService.findAll();
        if(!cameras.isEmpty()) {
                logger.info("<<==================== expire file cleaner started {} =================>>",LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
                Phaser phaser = new Phaser(cameras.size());
                File file = new File(snapPath);
                Assert.isTrue(file.exists(),"snapshot fileFolder not exist");
                cameras.forEach(camera -> {
                    if(camera.getStrategy() != null) {
                        int preserveDays = camera.getStrategy().getPreserveDays();
                        executor.submit(() -> {
                            try {
                                logger.info("camera: {},preserve days: {} ", camera,
                                    preserveDays);
                                File[] files = file.listFiles(cFile -> cFile.getName().split("_")[1]
                                    .equals(camera.getId().toString()));
                                if (files.length != 0) {
                                    String pattern = Pattern.quote(System.getProperty("file.separator"));
                                    int pathLength = file.getAbsolutePath().split(pattern).length;
                                    List<File> paths = Lists.newArrayList();
                                    paths = BatchRecordDeleteTask.this
                                        .fileList(files[0], paths, pathLength);
                                    paths.forEach(file1 -> {
                                        String[] time = file1.getAbsolutePath().split(pattern);
                                        Assert.isTrue(time.length >= 3, "file path error");
                                        int year = Integer.valueOf(time[time.length - 3]);
                                        int month = Integer.valueOf(time[time.length - 2]);
                                        int day = Integer.valueOf(time[time.length - 1]);
                                        LocalDate now = LocalDate.now().minusDays(preserveDays);
                                        if (LocalDate.of(year, month, day).isBefore(now)) {
                                            FileUtils.deleteQuietly(file1);
                                            logger.info(
                                                "<<==================== expire file delete cameraId:{},path:{} =================>>",
                                                camera.getId(), file1.getAbsolutePath());
                                        }
                                    });
                                }
                            } catch (RuntimeException e) {
                                logger.error("catch an runtimeException when deleting file {}", e.getMessage());
                            } finally {
                                phaser.arrive();
                            }
                        });
                    }
                });
              phaser.awaitAdvance(phaser.getPhase());
           }
            logger.info("<<==================== expire file cleaner completed {} =================>>",LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
        }

    @Scheduled(cron = "${system.recordSchedule: 00 00 03 * * ? * }")
    public void clean() {
        logger.info("<<==================== expire record cleaner started {}=================>>",LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
        List<Camera> cameras = cameraService.findAll();
        cameras.forEach(camera -> {
            try {
                if(camera.getStrategy() != null) {
                    int preserveDays = camera.getStrategy().getPreserveDays();
                    logger.info("camera: {},preserve days: {} ", camera, preserveDays);
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime time = now.minusDays(preserveDays).truncatedTo(ChronoUnit.DAYS);
                    Timestamp timestamp = Timestamp.valueOf(time);
                    int n2 = monitorRecordService.deleteBySnapshotCameraIdAndSnapshotTimestampLessThan(camera.getId(),
                        timestamp.getTime());
                    logger.info("remove [{}] monitorRecords of cameraId [{}]", n2, camera.getId());
                    int n1 = snapShotRecordService
                        .deleteByCameraIdAndTimestampLessThan(camera.getId(), timestamp.getTime());
                    logger.info("remove [{}] snapshotRecords of cameraId [{}]", n1, camera.getId());
                }
            } catch (Exception e) {
               logger.error("error happens when delete record {}",e);
            }
        });
        logger.info("<<==================== expire record cleaner completed {} =================>>",LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
    }


    private List<File> fileList(File file,List<File> resultFileName,int pathLength){
        File[] files = file.listFiles();
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        if(files==null)return resultFileName;
        for (File f : files) {
            int path = f.getAbsolutePath().split(pattern).length;
            if (f.isDirectory() ) {
                if(path-pathLength == 4) {
                    resultFileName.add(f);
                }
                fileList(f, resultFileName,pathLength);
            }
        }
        return resultFileName;
    }
}

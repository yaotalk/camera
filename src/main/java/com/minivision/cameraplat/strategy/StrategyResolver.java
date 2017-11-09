package com.minivision.cameraplat.strategy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.minivision.cameraplat.domain.Camera;
import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.domain.MonitorImage;
import com.minivision.cameraplat.domain.Strategy;
import com.minivision.cameraplat.domain.record.MonitorRecord;
import com.minivision.cameraplat.domain.record.SnapshotRecord;
import com.minivision.cameraplat.repository.MonitorRecordRepository;
import com.minivision.cameraplat.repository.SnapshotRecordRepository;
import com.minivision.cameraplat.util.CommonUtils;

public abstract class StrategyResolver {
  
  @Autowired
  private MonitorRecordRepository monitorRepository;
  @Autowired
  private SnapshotRecordRepository snapshotRespository;
  @Autowired
  private SimpMessagingTemplate template;
  
  @Value("${system.store.snapshot}")
  private String imageFilePath = ".";

  private String imageFileType = "jpg";
  
  protected void saveMonitorImage(MonitorImage image){
    if(image.getFileName() == null){
      //String fileName = image.getSerialNo() + "_" + image.getCameraId() + "_" + getDatePath() + "." + imageFileType;
      String fileName = "camera_"+image.getCameraId() + "/" + CommonUtils.getDatePath() + "/" + image.getSerialNo() + "." + imageFileType;
      File imgFile = new File(imageFilePath, fileName);
      try {
        //FileUtils.touch(imgFile);
        FileUtils.writeByteArrayToFile(imgFile, image.getImage());
        image.setFileName(fileName);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  protected void saveSnapShot(MonitorImage image, SnapshotRecord snapshotRecord){
    saveMonitorImage(image);
    snapshotRecord.setPhotoFileName(image.getFileName());
    snapshotRespository.save(snapshotRecord);
    template.convertAndSend("/c/snapshot", snapshotRecord);
  }

  protected void saveAlarm(SnapshotRecord snapshotRecord, Face face){
    MonitorRecord record = new MonitorRecord(snapshotRecord);
    if(face!=null){
      record.setFace(face);
    }
    monitorRepository.save(record);
    template.convertAndSend("/c/alarm", record);
  }
  
  public abstract void onRecognized(Camera camera, MonitorImage image, SnapshotRecord snapshotRecord, String faceToken);
  
  public abstract void onStranger(Camera camera, MonitorImage image, SnapshotRecord snapshotRecord);

}

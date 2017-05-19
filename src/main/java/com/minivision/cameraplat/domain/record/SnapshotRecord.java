package com.minivision.cameraplat.domain.record;

import com.minivision.cameraplat.domain.EntranceGuard;
import com.minivision.cameraplat.domain.IdEntity;
import com.minivision.cameraplat.domain.MonitorImage;
import com.minivision.cameraplat.faceplat.result.detect.DetectedFace;

import javax.persistence.*;
import java.util.Set;

@Entity
public class SnapshotRecord extends IdEntity{
  private long timestamp;
  private long cameraId;
  private String photoFileName;
  
  private float confidence;

  @Transient
  private int isOut;
  @Transient
  private String padId;
  @Transient
  private Set<EntranceGuard> entranceGuards;
  @Transient
  private String doorNumber;
  @Embedded
  private FacePos facePosition;

  public SnapshotRecord() {
  }

  public SnapshotRecord(MonitorImage image, DetectedFace face){
    this.timestamp = image.getTimestamp();
    this.cameraId = image.getCameraId();
    this.photoFileName = image.getFileName();
    this.facePosition = new FacePos();
    this.facePosition.top = face.getFaceRectangle().getTop();
    this.facePosition.left = face.getFaceRectangle().getLeft();
    this.facePosition.width = face.getFaceRectangle().getWidth();
    this.facePosition.height = face.getFaceRectangle().getHeight();
    
  }

  public String getDoorNumber() {
    return doorNumber;
  }

  public void setDoorNumber(String doorNumber) {
    this.doorNumber = doorNumber;
  }

  public int getIsOut() {
    return isOut;
  }

  public void setIsOut(int isOut) {
    this.isOut = isOut;
  }

  public String getPadId() {
    return padId;
  }

  public void setPadId(String padId) {
    this.padId = padId;
  }

  public Set<EntranceGuard> getEntranceGuards() {
    return entranceGuards;
  }

  public void setEntranceGuards(Set<EntranceGuard> entranceGuards) {
    this.entranceGuards = entranceGuards;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getCameraId() {
    return cameraId;
  }

  public void setCameraId(long cameraId) {
    this.cameraId = cameraId;
  }

  public String getPhotoFileName() {
    return photoFileName;
  }

  public void setPhotoFileName(String photoFileName) {
    this.photoFileName = photoFileName;
  }

  public FacePos getFacePosition() {
    return facePosition;
  }

  public void setFacePosition(FacePos facePosition) {
    this.facePosition = facePosition;
  }
  
  public float getConfidence() {
    return confidence;
  }

  public void setConfidence(float confidence) {
    this.confidence = confidence;
  }



  @Embeddable
  public static class FacePos{
    @Column(name="pos_top")
    private int top;
    @Column(name="pos_left")
    private int left;
    private int width;
    private int height;
    public int getTop() {
      return top;
    }
    public void setTop(int top) {
      this.top = top;
    }
    public int getLeft() {
      return left;
    }
    public void setLeft(int left) {
      this.left = left;
    }
    public int getWidth() {
      return width;
    }
    public void setWidth(int width) {
      this.width = width;
    }
    public int getHeight() {
      return height;
    }
    public void setHeight(int height) {
      this.height = height;
    }
  }
}

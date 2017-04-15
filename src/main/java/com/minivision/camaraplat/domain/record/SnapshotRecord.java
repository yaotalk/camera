package com.minivision.camaraplat.domain.record;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.minivision.camaraplat.domain.IdEntity;
import com.minivision.camaraplat.domain.MonitorImage;
import com.minivision.camaraplat.faceplat.result.detect.DetectedFace;

@Entity
public class SnapshotRecord extends IdEntity{
  private long timestamp;
  private long cameraId;
  private String photoFileName;
  
  private float confidence;
  
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

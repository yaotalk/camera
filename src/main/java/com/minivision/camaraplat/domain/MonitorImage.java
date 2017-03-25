package com.minivision.camaraplat.domain;

public class MonitorImage {
  private long serialNo;
  private long timestamp;
  private int cameraId;
  private byte[] image;
  
  public MonitorImage(long serialNo, long timestamp, int cameraId, byte[] image) {
    this.serialNo = serialNo;
    this.timestamp = timestamp;
    this.cameraId = cameraId;
    this.image = image;
  }
  public long getSerialNo() {
    return serialNo;
  }
  public void setSerialNo(long serialNo) {
    this.serialNo = serialNo;
  }
  public long getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
  public int getCameraId() {
    return cameraId;
  }
  public void setCameraId(int cameraId) {
    this.cameraId = cameraId;
  }
  public byte[] getImage() {
    return image;
  }
  public void setImage(byte[] image) {
    this.image = image;
  }
  
  @Override
  public String toString() {
    return "MonitorImage [serialNo=" + serialNo + ", timestamp=" + timestamp + ", cameraId="
        + cameraId + ", image_size=" + image.length + "]";
  }
  
}

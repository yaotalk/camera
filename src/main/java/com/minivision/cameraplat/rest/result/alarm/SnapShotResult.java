package com.minivision.cameraplat.rest.result.alarm;

public class SnapShotResult {
  private long snapTime;
  private String sex;
  private String imgUrl;
  private long count;
  private float confidence;
  private FacePostion facePostion;
  public SnapShotResult() {
  }

  public float getConfidence() {
    return confidence;
  }

  public void setConfidence(float confidence) {
    this.confidence = confidence;
  }

  public long getSnapTime() {
    return snapTime;
  }

  public void setSnapTime(long snapTime) {
    this.snapTime = snapTime;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public FacePostion getFacePostion() {
    return facePostion;
  }

  public void setFacePostion(FacePostion facePostion) {
    this.facePostion = facePostion;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public SnapShotResult(long snapTime, String sex, String imgUrl, long count,float confidence,
      FacePostion facePostion) {
    this.snapTime = snapTime;
    this.sex = sex;
    this.imgUrl = imgUrl;
    this.count = count;
    this.confidence = confidence;
    this.facePostion = facePostion;
  }
  public static  class FacePostion {
    private int top;
    private int left;
    private int width;
    private int height;

    public FacePostion(int top, int left, int width, int height) {
      this.top = top;
      this.left = left;
      this.width = width;
      this.height = height;
    }

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

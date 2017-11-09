package com.minivision.cameraplat.domain.record;

import com.minivision.cameraplat.domain.EntranceGuard;
import com.minivision.cameraplat.domain.IdEntity;
import com.minivision.cameraplat.domain.MonitorImage;
import com.minivision.cameraplat.faceplat.result.detect.DetectedFace;

import java.util.Set;

import javax.persistence.*;


@Entity
public class SnapshotRecord extends IdEntity{
  private long timestamp;
  private long cameraId;
  private String photoFileName;
  private String photoUrl;
  private float confidence;
  @Embedded
  private SnapShotFace face;
  
  @Transient
  private int isOut;
  @Transient
  private String padId;
  @Transient
  private Set<EntranceGuard> entranceGuards;
  @Transient
  private String doorNumber;
  
  public SnapshotRecord() {
    
  }

  public SnapshotRecord(MonitorImage image, DetectedFace face){
    this.timestamp = image.getTimestamp();
    this.cameraId = image.getCameraId();
    this.photoFileName = image.getFileName();
    this.photoUrl = image.getFileName();
    
    SnapShotFace.FacePos facePosition = new SnapShotFace.FacePos();
    facePosition.top = face.getFaceRectangle().getTop();
    facePosition.left = face.getFaceRectangle().getLeft();
    facePosition.width = face.getFaceRectangle().getWidth();
    facePosition.height = face.getFaceRectangle().getHeight();
    
    SnapShotFace.FaceAttr faceAttr = new SnapShotFace.FaceAttr();
    faceAttr.age = face.getFaceAttribute().getAge();
    faceAttr.ageConfidence = face.getFaceAttribute().getAgeConfidence();
    faceAttr.gender = face.getFaceAttribute().getGender();
    faceAttr.genderConfidence = face.getFaceAttribute().getGenderConfidence();
    
    SnapShotFace shotFace = new SnapShotFace();
    shotFace.facePosition = facePosition;
    shotFace.faceAttributes = faceAttr;
    
    this.face = shotFace;
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

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public SnapShotFace getFace() {
    return face;
  }

  public void setFace(SnapShotFace face) {
    this.face = face;
  }

  public float getConfidence() {
    return confidence;
  }

  public void setConfidence(float confidence) {
    this.confidence = confidence;
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

  public String getDoorNumber() {
    return doorNumber;
  }

  public void setDoorNumber(String doorNumber) {
    this.doorNumber = doorNumber;
  }



  @Embeddable
  public static class SnapShotFace{
    @Embedded
    private FacePos facePosition;
    @Embedded
    private FaceAttr faceAttributes;
    
    public FacePos getFacePosition() {
      return facePosition;
    }

    public void setFacePosition(FacePos facePosition) {
      this.facePosition = facePosition;
    }

    public FaceAttr getFaceAttributes() {
      return faceAttributes;
    }

    public void setFaceAttributes(FaceAttr faceAttributes) {
      this.faceAttributes = faceAttributes;
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
    
    @Embeddable
    public static class FaceAttr{
      private int age;
      private double ageConfidence;
      private int gender;
      private double genderConfidence;
      public int getAge() {
        return age;
      }
      public void setAge(int age) {
        this.age = age;
      }
      public double getAgeConfidence() {
        return ageConfidence;
      }
      public void setAgeConfidence(double ageConfidence) {
        this.ageConfidence = ageConfidence;
      }
      public int getGender() {
        return gender;
      }
      public void setGender(int gender) {
        this.gender = gender;
      }
      public double getGenderConfidence() {
        return genderConfidence;
      }
      public void setGenderConfidence(double genderConfidence) {
        this.genderConfidence = genderConfidence;
      }
    }
  }


}

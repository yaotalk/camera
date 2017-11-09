package com.minivision.cameraplat.domain.record;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.domain.IdEntity;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@Entity
@JsonInclude(Include.NON_EMPTY)
public class MonitorRecord extends IdEntity{
  @OneToOne(cascade =CascadeType.REMOVE)
  @JoinColumn(name="snapshot")
  private SnapshotRecord snapshot;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="faceToken", foreignKey = @ForeignKey(name = "none" , value= ConstraintMode.NO_CONSTRAINT))
  @NotFound(action = NotFoundAction.IGNORE)
  private Face face;

  @Transient
  @ApiModelProperty(hidden = true)
  private List<FaceInfo> faceInfos;

  public List<FaceInfo> getFaceInfos() {
    return faceInfos;
  }

  public void setFaceInfos(List<FaceInfo> faceInfos) {
    this.faceInfos = faceInfos;
  }

  public MonitorRecord() {
  }

  public MonitorRecord(SnapshotRecord snapshot) {
    this.snapshot = snapshot;
  }
  
  public SnapshotRecord getSnapshot() {
    return snapshot;
  }
  public void setSnapshot(SnapshotRecord snapshot) {
    this.snapshot = snapshot;
  }
  public Face getFace() {
    return face;
  }
  public void setFace(Face face) {
    this.face = face;
  }

  public static class FaceInfo{
    private Face face;
    private float confidence;

    public FaceInfo(Face face, float confidence) {
      this.face = face;
      this.confidence = confidence;
    }

    public Face getFace() {
      return face;
    }

    public void setFace(Face face) {
      this.face = face;
    }

    public float getConfidence() {
      return confidence;
    }

    public void setConfidence(float confidence) {
      this.confidence = confidence;
    }
  }
}

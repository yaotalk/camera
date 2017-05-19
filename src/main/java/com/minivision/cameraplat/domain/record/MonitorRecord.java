package com.minivision.cameraplat.domain.record;

import javax.persistence.CascadeType;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.domain.IdEntity;

@Entity
public class MonitorRecord extends IdEntity{
  @OneToOne
  @JoinColumn(name="snapshot")
  private SnapshotRecord snapshot;
  @ManyToOne(fetch = FetchType.LAZY, cascade =CascadeType.REMOVE)
  @JoinColumn(name="faceToken", foreignKey = @ForeignKey(name = "none" , value= ConstraintMode.NO_CONSTRAINT))
  @NotFound(action = NotFoundAction.IGNORE)
  private Face face;
  
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

}

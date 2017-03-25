package com.minivision.camaraplat.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Entity
public class AnalyserStatus extends IdEntity {
  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  @JoinColumn(name = "analyser_id")
  @JsonUnwrapped(prefix="analyser_")
  private Analyser analyser;
  private float cpu;
  private float mem;
  @Temporal(TemporalType.TIMESTAMP)
  private Date timestamp;

  public Analyser getAnalyser() {
    return analyser;
  }

  public void setAnalyser(Analyser analyser) {
    this.analyser = analyser;
  }

  public float getCpu() {
    return cpu;
  }

  public void setCpu(float cpu) {
    this.cpu = cpu;
  }

  public float getMem() {
    return mem;
  }

  public void setMem(float mem) {
    this.mem = mem;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "AnalyserStatus [analyser=" + analyser.getId() + ", cpu=" + cpu + ", mem="
        + mem + ", timestamp=" + timestamp + "]";
  }
  
  
}

package com.minivision.camaraplat.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.minivision.camaraplat.mqtt.message.MsgCameraStrategy;

@Entity
public class Strategy extends IdEntity {
  private String name;
  private String strategyType;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "scheme_id")
  @JsonView(MsgCameraStrategy.class)
  @JsonUnwrapped(prefix="scheme_")
  private Scheme scheme;
  private int maxfaceNum;
  private int compareThreshold;
  private int minFaceSize;
  private int preserveDays;
  @JsonView(MsgCameraStrategy.class)
  private int faceQualityThreshold;
  private int intervals;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStrategyType() {
    return strategyType;
  }

  public void setStrategyType(String strategyType) {
    this.strategyType = strategyType;
  }

  public Scheme getScheme() {
    return scheme;
  }

  public void setScheme(Scheme scheme) {
    this.scheme = scheme;
  }

  public int getMaxfaceNum() {
    return maxfaceNum;
  }

  public void setMaxfaceNum(int maxfaceNum) {
    this.maxfaceNum = maxfaceNum;
  }

  public int getCompareThreshold() {
    return compareThreshold;
  }

  public void setCompareThreshold(int compareThreshold) {
    this.compareThreshold = compareThreshold;
  }

  public int getMinFaceSize() {
    return minFaceSize;
  }

  public void setMinFaceSize(int minFaceSize) {
    this.minFaceSize = minFaceSize;
  }

  public int getPreserveDays() {
    return preserveDays;
  }

  public void setPreserveDays(int preserveDays) {
    this.preserveDays = preserveDays;
  }

  public int getFaceQualityThreshold() {
    return faceQualityThreshold;
  }

  public void setFaceQualityThreshold(int faceQualityThreshold) {
    this.faceQualityThreshold = faceQualityThreshold;
  }

  public int getIntervals() {
    return intervals;
  }

  public void setIntervals(int intervals) {
    this.intervals = intervals;
  }

}

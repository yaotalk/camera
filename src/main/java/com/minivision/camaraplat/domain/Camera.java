package com.minivision.camaraplat.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.minivision.camaraplat.mqtt.message.MsgAnalyserConfig;

import java.util.Set;
@Entity
public class Camera extends IdEntity {
  
  @JsonView(MsgAnalyserConfig.class)
  private int type;
  @JsonView(MsgAnalyserConfig.class)
  private String ip;
  @JsonView(MsgAnalyserConfig.class)
  private String username;
  @JsonView(MsgAnalyserConfig.class)
  private String password;
  private String deviceName;
  @JsonView(MsgAnalyserConfig.class)
  private int port;
  @JsonView(MsgAnalyserConfig.class)
  private int webPort;
  @JsonView(MsgAnalyserConfig.class)
  private int rtspPort;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "strategy_id")
  @JsonView(MsgAnalyserConfig.class)
  @JsonUnwrapped(prefix="strategy_")
  private Strategy strategy;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "analyser_id")
  private Analyser analyser;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "region_id")
  private Region region;

//  @JsonIgnore
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "cam_fs", joinColumns = {@JoinColumn(name = "camera_id")},
      inverseJoinColumns = {@JoinColumn(name = "face_token")})
  private Set<FaceSet> faceSets;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getRtspPort() {
    return rtspPort;
  }

  public void setRtspPort(int rtspPort) {
    this.rtspPort = rtspPort;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }


  public Strategy getStrategy() {
    return strategy;
  }

  public void setStrategy(Strategy strategy) {
    this.strategy = strategy;
  }

  public Analyser getAnalyser() {
    return analyser;
  }

  public void setAnalyser(Analyser analyser) {
    this.analyser = analyser;
  }

  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  public Set<FaceSet> getFaceSets() {
    return faceSets;
  }

  public void setFaceSets(Set<FaceSet> faceSets) {
    this.faceSets = faceSets;
  }

  public int getWebPort() {
    return webPort;
  }

  public void setWebPort(int webPort) {
    this.webPort = webPort;
  }

}

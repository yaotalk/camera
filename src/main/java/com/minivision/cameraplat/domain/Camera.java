package com.minivision.cameraplat.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.minivision.cameraplat.mqtt.message.MsgAnalyserConfig;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
  @JsonView(MsgAnalyserConfig.class)
  private String rtspUrl;

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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "faceset_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  private FaceSet faceSet;

//  @JsonIgnore
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "cam_fs", joinColumns = {@JoinColumn(name = "camera_id")},
      inverseJoinColumns = {@JoinColumn(name = "face_token")})
  private Set<FaceSet> faceSets;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "cam_door",joinColumns = {@JoinColumn(name = "camera_id")},inverseJoinColumns = {@JoinColumn(name = "door_id")})
  private List<EntranceGuard.Door> doors;

  private int isOut;

  private String padId;

  private String doorNumber;

  public FaceSet getFaceSet() {
    return faceSet;
  }

  public void setFaceSet(FaceSet faceSet) {
    this.faceSet = faceSet;
  }

  public String getRtspUrl() {
    return rtspUrl;
  }

  public void setRtspUrl(String rtspUrl) {
    this.rtspUrl = rtspUrl;
  }

  public String getDoorNumber() {
    return doorNumber;
  }

  public void setDoorNumber(String doorNumber) {
    this.doorNumber = doorNumber;
  }

  public List<EntranceGuard.Door> getDoors() {
    return doors;
  }

  public void setDoors(List<EntranceGuard.Door> doors) {
    this.doors = doors;
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

  @Override public String toString() {

    return "Camera{id="  + id + '\'' + ",DeviceType=" + type + ", ip='" + ip + '\'' + ", username='" + username + '\''
        + ", password='" + password + '\'' + ", deviceName='" + deviceName + '\'' + ", port=" + port
        + ", webPort=" + webPort + ", rtspPort=" + rtspPort + ", strategy=" + (strategy==null?null:strategy.getId())
        + ", analyser=" + (analyser==null?null:analyser.getId()) + ", region=" + (region==null?null:region.getId())+ ", faceSets=" + (faceSets==null?null:
        faceSets.stream().map(FaceSet::getToken).collect(Collectors.toList())) +
        ", in or out（0 in, 1 out）=" + isOut + ", padId='" + padId + '\'' +'\'' + '}';
  }
}

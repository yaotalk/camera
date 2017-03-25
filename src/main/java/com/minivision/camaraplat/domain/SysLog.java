package com.minivision.camaraplat.domain;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yao on 2017/3/12.
 */
@Entity
public class SysLog extends IdEntity {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "userid")
  private User user;
  private String ip;
  private String modelName; // 操作模块
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;
  private String opration;

  public SysLog(User user, String ip, String modelName, String opration, Date createTime) {
    this.user = user;
    this.ip = ip;
    this.modelName = modelName;
    this.opration = opration;
    this.createTime = createTime;
  }

  public SysLog() {}

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getCreateTime() throws Exception {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time = sf.format(createTime);
    return time;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getOpration() {
    return opration;
  }

  public void setOpration(String opration) {
    this.opration = opration;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

}

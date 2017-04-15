package com.minivision.camaraplat.domain;

import com.minivision.camaraplat.config.EncodeConfig;

import javax.persistence.Entity;

/**
 * Created by yao on 2017/3/12.
 */
@Entity
public class User extends IdEntity {
  private String username;
  private String password;
  private String roleid;
  private boolean autologin;

  public boolean isAutologin() {
    return autologin;
  }

  public void setAutologin(boolean autologin) {
    this.autologin = autologin;
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
    password = EncodeConfig.EncodeByMd5(password);
    this.password = password;
  }

  public String getRoleid() {
    return roleid;
  }

  public void setRoleid(String roleid) {
    this.roleid = roleid;
  }
}

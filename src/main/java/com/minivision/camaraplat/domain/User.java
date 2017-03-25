package com.minivision.camaraplat.domain;

import javax.persistence.Entity;

/**
 * Created by yao on 2017/3/12.
 */
@Entity
public class User extends IdEntity {
  private String username;
  private String password;
  private String roleid;

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

  public String getRoleid() {
    return roleid;
  }

  public void setRoleid(String roleid) {
    this.roleid = roleid;
  }
}

package com.minivision.camaraplat.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;

//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

@Entity(name="users")
public class User extends IdEntity {
  @Column(unique = true)
  private String username;
  private String password;
  private boolean enabled;

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

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//    // TODO Auto-generated method stub
//    return null;
//  }

//  @Override
//  public boolean isAccountNonExpired() {
//    // TODO Auto-generated method stub
//    return false;
//  }
//
//  @Override
//  public boolean isAccountNonLocked() {
//    // TODO Auto-generated method stub
//    return false;
//  }
//
//  @Override
//  public boolean isCredentialsNonExpired() {
//    // TODO Auto-generated method stub
//    return false;
//  }

}

package com.minivision.camaraplat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class FaceSet {

  @Id
  @NotEmpty(message = "token is required.")
  private String token;

  @Column(name = "faceset_name")
  @NotEmpty(message = "name is required.")
  private String name;

  @Column(name = "faceset_status", length = 4)
  private int status;

  @Transient
  private int faceCount;

  @Temporal(TemporalType.DATE)
  private Date createTime;

  public Set<Face> getFaces() {
    return faces;
  }

  public void setFaces(Set<Face> faces) {
    this.faces = faces;
  }
  @JsonIgnore
  @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY,mappedBy="faceSet")
  private Set<Face> faces;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getFaceCount() {
    return faceCount;
  }

  public void setFaceCount(int faceCount) {
    this.faceCount = faceCount;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FaceSet other = (FaceSet) obj;
    if (token == null) {
      if (other.token != null)
        return false;
    } else if (!token.equals(other.token))
      return false;
    return true;
  }

  // TODO
  
}

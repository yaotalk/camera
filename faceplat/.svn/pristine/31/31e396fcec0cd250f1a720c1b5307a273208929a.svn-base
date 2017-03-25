package com.minivision.faceplat.entity;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("faces")
public class Face implements Serializable {

  private static final long serialVersionUID = -6529949396986802348L;

  @Id
  private String token;
  @Indexed
  private String outerId;

  private double[] future;
  private int referenceCnt;
  
  //do not save in redis?
  @Transient
  private byte[] img;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getOuterId() {
    return outerId;
  }

  public void setOuterId(String outerId) {
    this.outerId = outerId;
  }

  public byte[] getImg() {
    return img;
  }

  public void setImg(byte[] img) {
    this.img = img;
  }

  public double[] getFuture() {
    return future;
  }

  public void setFuture(double[] future) {
    this.future = future;
  }

  public int getReferenceCnt() {
    return referenceCnt;
  }

  public void setReferenceCnt(int referenceCnt) {
    this.referenceCnt = referenceCnt;
  }

  @Override
  public String toString() {
    return "Face [token=" + token + ", outerId=" + outerId + ", future=" + Arrays.toString(future)
        + ", referenceCnt=" + referenceCnt + "]";
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
    Face other = (Face) obj;
    if (token == null) {
      if (other.token != null)
        return false;
    } else if (!token.equals(other.token))
      return false;
    return true;
  }

}

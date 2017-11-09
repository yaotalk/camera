package com.minivision.cameraplat.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Face {
  @Id
  @NotEmpty(message = "Id is required.")
  private String id;
  private String name;
  private String sex;
  private String idCard;
  private String employeeId;
  private Integer picSize;
  private String picMd5;
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;

  public Face() {}

  public Face(String name,String idCard, String sex, String phoneNumber) {
    this.name = name;
    this.idCard = idCard;
    this.sex = sex;
    this.phoneNumber = phoneNumber;
  }

  public Face(String name, String sex, String idCard, String phoneNumber, FaceSet faceSet,
      String imgpath) {
    this.name = name;
    this.sex = sex;
    this.idCard = idCard;
    this.phoneNumber = phoneNumber;
    this.faceSet = faceSet;
    this.imgpath = imgpath;
  }

  private String phoneNumber;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "faceset_id",
      foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  private FaceSet faceSet;
  private String imgpath;

  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getImgpath() {
    return imgpath;
  }

  public void setImgpath(String imgpath) {
    this.imgpath = imgpath;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public FaceSet getFaceSet() {
    return faceSet;
  }

  public void setFaceSet(FaceSet faceSet) {
    this.faceSet = faceSet;
  }

  public Integer getPicSize() {
    return picSize;
  }

  public void setPicSize(Integer picSize) {
    this.picSize = picSize;
  }

  public String getPicMd5() {
    return picMd5;
  }

  public void setPicMd5(String picMd5) {
    this.picMd5 = picMd5;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "Face{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", sex（0 womam 1 man）='" + sex + '\''
        + ", IDCard ='" + idCard + '\'' + ", employeeId ='" + employeeId + '\'' + ", phoneNumber='" + phoneNumber
        + '\'' + ", FaceDB=" + faceSet.getToken() + ", pic path ='" + imgpath + '\'' + ", picSize ='"
        + picSize + '\'' + ", pic MD5 ='" + picMd5 + '\'' + '}';
  }

}

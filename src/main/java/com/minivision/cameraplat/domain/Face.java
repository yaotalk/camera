package com.minivision.cameraplat.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/3/15 0015.
 */
@Entity
public class Face {
    @Id
    @NotEmpty(message = "Id is required.")
    private String id;
    private String name;
    private String sex;
    private String idCard;
    private String employeeId;

    public Face() {
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
    @JoinColumn(name = "faceset_id", foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
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

    @Override public String toString() {
        return "人脸{" + "id='" + id + '\'' + ", 姓名='" + name + '\'' + ", 性别（0男1女）='" + sex + '\''
            + ", 身份证号='" + idCard + '\'' + ", 工号='" + employeeId + '\''
            + ", 电话号码='" + phoneNumber + '\'' + ", 所属人脸库token=" + faceSet.getToken() + ", 图片路径='"
            + imgpath + '\'' + '}';
    }
}

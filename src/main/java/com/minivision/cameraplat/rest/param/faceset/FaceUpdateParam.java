package com.minivision.cameraplat.rest.param.faceset;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class FaceUpdateParam {

    @NotBlank(message ="faceSetToken must not be null" )
    private String facesetToken;

    @NotBlank(message ="faceToken must not be null" )
    private String faceToken;

    private String name;

    private String idCard;

    @ApiModelProperty(value = "'0'女'1'男")
    private String sex;

    private String phoneNumber;

//    @NotBlank(message = "imgFile must not be null")
    private MultipartFile imgFile;

    public String getFacesetToken() {
        return facesetToken;
    }

    public void setFacesetToken(String facesetToken) {
        this.facesetToken = facesetToken;
    }

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public MultipartFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override public String toString() {
        return "FaceUpdateParam{" + "facesetToken='" + facesetToken + '\'' + ", faceToken='"
            + faceToken + '\'' + ", name='" + name + '\'' + ", idCard='" + idCard + '\'' + ", sex='"
            + sex + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", imgFile=" + Optional.ofNullable(imgFile).orElse(null) + '}';
    }
}

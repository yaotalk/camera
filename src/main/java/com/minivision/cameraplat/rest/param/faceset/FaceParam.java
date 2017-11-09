package com.minivision.cameraplat.rest.param.faceset;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;

public class FaceParam {

    @NotNull(message = "facesetToken must not be null")
    @ApiParam(required = true)
    private String facesetToken;

    private String name;

    private String idCard;

    @ApiModelProperty(value = "'0'女'1'男")
    private String sex;

    private String phoneNumber;

    @NotNull(message = "file must not be null")
    @ApiModelProperty(value = "图片文件")
    private MultipartFile imgFile;

    public String getFacesetToken() {
        return facesetToken;
    }

    public void setFacesetToken(String facesetToken) {
        this.facesetToken = facesetToken;
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

    public MultipartFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
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
        return "FaceParam{" + "facesetToken='" + facesetToken + '\'' + ", name='" + name + '\''
            + ", idCard='" + idCard + '\'' + ", imgFile=" + imgFile.getSize() + '}';
    }
}

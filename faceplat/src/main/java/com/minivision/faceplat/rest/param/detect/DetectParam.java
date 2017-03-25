package com.minivision.faceplat.rest.param.detect;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.minivision.faceplat.rest.param.RestParam;

public class DetectParam extends RestParam {

	private static final long serialVersionUID = -292864702752714956L;
	
	private String imageUrl;
	
	@NotNull(message = "imageFile must not be empty")
	private MultipartFile imageFile;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	@Override
	public String toString() {
		return "DetectParam [imageUrl=" + imageUrl + ", imageFile=" + imageFile + ", apiKey=" + apiKey + ", apiSecret="
				+ apiSecret + "]";
	}

}

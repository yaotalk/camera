package com.minivision.faceplat.rest.param.faceset;

import org.hibernate.validator.constraints.NotBlank;

import com.minivision.faceplat.rest.param.RestParam;

public class SetModifyParam extends RestParam {

	private static final long serialVersionUID = 7184949388454547512L;

	@NotBlank(message = "facesetToken must not be empty")
	private String facesetToken;
	
	private String displayName;

	public String getFacesetToken() {
		return facesetToken;
	}

	public void setFacesetToken(String facesetToken) {
		this.facesetToken = facesetToken;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}

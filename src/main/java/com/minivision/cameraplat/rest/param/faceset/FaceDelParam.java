package com.minivision.cameraplat.rest.param.faceset;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class FaceDelParam {

    @NotNull(message = "faceTokens must not be null")
    @ApiModelProperty(value = "逗号分割",required = true)
    private String faceTokens;

    @NotNull(message = "faceSetToken must not be null")
    @ApiModelProperty(required = true)
    private String facetSetToken;

    public String getFacetSetToken() {
        return facetSetToken;
    }

    public void setFacetSetToken(String facetSetToken) {
        this.facetSetToken = facetSetToken;
    }

    public String getFaceTokens() {
        return faceTokens;
    }

    public void setFaceTokens(String faceTokens) {
        this.faceTokens = faceTokens;
    }

    @Override public String toString() {
        return "FaceDelParam{" + "faceTokens='" + faceTokens + '\'' + ", facetSetToken='"
            + facetSetToken + '\'' + '}';
    }
}

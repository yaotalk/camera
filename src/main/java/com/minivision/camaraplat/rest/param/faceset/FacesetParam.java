package com.minivision.camaraplat.rest.param.faceset;

import com.minivision.camaraplat.rest.param.RestParam;
import io.swagger.annotations.ApiParam;

import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/3/28 0028.
 */
public class FacesetParam extends RestParam {

    @ApiParam(required = true)
    @NotBlank(message = "faceToken must not be null")
    private String facesetToken;
    
    private int offset = 0;
    
    @Max(100)
    private int limit = 10;

    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getFacesetToken() {
        return facesetToken;
    }

    public void setFacesetToken(String facesetToken) {
        this.facesetToken = facesetToken;
    }

    public int getOffset() {
      return offset;
    }

    public void setOffset(int offset) {
      this.offset = offset;
    }

    public int getLimit() {
      return limit;
    }

    public void setLimit(int limit) {
      this.limit = limit;
    }
    
}

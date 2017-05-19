package com.minivision.cameraplat.rest.param.alarm;
import com.minivision.cameraplat.rest.param.RestParam;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

public class AlarmParam extends RestParam {

    private static final long serialVersionUID = -5324699653300472053L;

//    @NotBlank(message = "startTime can not be empty")
    private long startTime;

//    @NotBlank(message = "endTime can not be empty")
    private long endTime;

    @ApiParam(required = true)
    @ApiModelProperty(value = "摄像机ID")
    private long cameraId;

    private String name;

    private String sex;

    @ApiParam(required = true)
    @ApiModelProperty(value = "报警类型")
    private String logType;

    public long getStartTime() {
      return startTime;
    }

    public void setStartTime(long startTime) {
      this.startTime = startTime;
    }

    public long getEndTime() {
      return endTime;
    }

    public void setEndTime(long endTime) {
      this.endTime = endTime;
    }

    public long getCameraId() {
        return cameraId;
    }

    public void setCameraId(long cameraId) {
        this.cameraId = cameraId;
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

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }
}

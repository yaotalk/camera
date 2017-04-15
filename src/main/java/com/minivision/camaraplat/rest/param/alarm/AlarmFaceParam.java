package com.minivision.camaraplat.rest.param.alarm;

import com.minivision.camaraplat.rest.param.RestParam;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;

public class AlarmFaceParam extends RestParam{

     private static final long serialVersionUID = 181434001367270631L;

     @ApiModelProperty(value = "日志ID",required = true)
     private long logid;

     @ApiModelProperty(value = "记录总数")
     @Max(100)
     private int limit = 10;

     public long getLogid() {
          return logid;
     }

     public void setLogid(long logid) {
          this.logid = logid;
     }

     public int getLimit() {
          return limit;
     }

     public void setLimit(int limit) {
          this.limit = limit;
     }

}

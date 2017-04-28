package com.minivision.camaraplat.rest;

import com.minivision.camaraplat.rest.param.alarm.AlarmFaceParam;
import com.minivision.camaraplat.rest.param.alarm.AlarmParam;
import com.minivision.camaraplat.rest.param.alarm.SnapShotParam;
import com.minivision.camaraplat.rest.result.RestResult;
import com.minivision.camaraplat.rest.result.alarm.AlarmResult;
import com.minivision.camaraplat.rest.result.alarm.AlarmFacesResult;
import com.minivision.camaraplat.rest.result.alarm.SnapShotResult;
import com.minivision.camaraplat.service.FaceService;
import com.minivision.camaraplat.service.MonitorRecordService;
import com.minivision.camaraplat.service.SnapShotRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1", method = RequestMethod.POST)
@Api(tags = "AlarmApi", value = "Alarm Apis")
public class AlarmApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(AlarmApi.class);

  @Autowired
  private SnapShotRecordService snapShotRecordService;

  @Autowired
  MonitorRecordService monitorRecordService;

  @Autowired
  FaceService faceService;

  @RequestMapping(value = "alarm")
  @ApiOperation(value = "报警查询", notes = "查询指定时间平台中某个设备的报警日志信息")
  public RestResult<List<AlarmResult>> getAlarmLog(
      @Valid @ModelAttribute AlarmParam param) {
    LOGGER.info("emergency search at api");
    List<AlarmResult> results = monitorRecordService.findMonitorRecords(param);
    LOGGER.info("emergency search at api");
    return new RestResult<>(results);
  }

  @RequestMapping(value = "alarmFaces")
  @ApiOperation(value = "报警照片", notes = "通过报警ID获取该报警对应的最接近的前N人")
  public RestResult<List<AlarmFacesResult>> getAlarmFace(@Valid @ModelAttribute AlarmFaceParam param) {
    LOGGER.info("emergencyFaces search at api");
    List<AlarmFacesResult> list = faceService.searchByFlatForAlarm(param);
    LOGGER.info("emergencyFaces search at api");
    return new RestResult<>(list);
  }

  @RequestMapping(value = "snapShot")
  @ApiOperation(value = "抓拍查询", notes = "获取平台固定时间段的抓拍信息")
  public RestResult<List<SnapShotResult>> getsnapShots(@Valid @ModelAttribute SnapShotParam param) {
    LOGGER.info("snapShot search at api");
    List<SnapShotResult> list = snapShotRecordService.findByTimeandCameraId(param);
    LOGGER.info("snapShot search at api");
    return new RestResult<>(list);
  }

}

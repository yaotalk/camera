package com.minivision.cameraplat.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.minivision.cameraplat.domain.record.MonitorRecord;
import com.minivision.cameraplat.domain.record.SnapshotRecord;
import com.minivision.cameraplat.rest.param.alarm.AlarmFaceParam;
import com.minivision.cameraplat.rest.param.alarm.AlarmParam;
import com.minivision.cameraplat.rest.param.alarm.SnapShotParam;
import com.minivision.cameraplat.rest.result.PageResult;
import com.minivision.cameraplat.rest.result.RestResult;
import com.minivision.cameraplat.rest.result.alarm.AlarmFacesResult;
import com.minivision.cameraplat.service.FaceService;
import com.minivision.cameraplat.service.MonitorRecordService;
import com.minivision.cameraplat.service.SnapShotRecordService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1",method = RequestMethod.GET)
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
  public RestResult<PageResult<MonitorRecord>> getAlarmLog(
      @Valid @ModelAttribute AlarmParam param) {
    PageResult<MonitorRecord> results = monitorRecordService.findMonitorRecords(param);
    return new RestResult<>(results);
  }

  @RequestMapping(value = "alarmFaces")
  @ApiOperation(value = "报警照片", notes = "通过报警ID获取该报警对应的最接近的前N人")
  public RestResult<List<AlarmFacesResult>> getAlarmFace(@Valid @ModelAttribute AlarmFaceParam param) {
    List<AlarmFacesResult> list = faceService.searchByFlatForAlarm(param);
    return new RestResult<>(list);
  }

  @RequestMapping(value = "snapShot")
  @ApiOperation(value = "抓拍查询", notes = "获取平台固定时间段的抓拍信息")
  public RestResult<PageResult<SnapshotRecord>> getsnapShots(@Valid @ModelAttribute SnapShotParam param) {
    PageResult<SnapshotRecord> list = snapShotRecordService.findByTimeandCameraId(param);
    return new RestResult<>(list);
  }

}

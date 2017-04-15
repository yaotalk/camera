package com.minivision.camaraplat.service;

import com.minivision.camaraplat.rest.param.alarm.SnapShotParam;
import com.minivision.camaraplat.rest.result.alarm.SnapShotResult;

import java.util.List;

public interface SnapShotRecordService {

        List<SnapShotResult> findByTimeandCameraId(SnapShotParam param);
}

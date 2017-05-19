package com.minivision.cameraplat.service;

import java.util.List;

import com.minivision.cameraplat.rest.param.alarm.SnapShotParam;
import com.minivision.cameraplat.rest.result.alarm.SnapShotResult;

public interface SnapShotRecordService {

        List<SnapShotResult> findByTimeandCameraId(SnapShotParam param);
}

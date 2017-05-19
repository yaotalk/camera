package com.minivision.cameraplat.service;

import java.util.List;

import com.minivision.cameraplat.rest.param.alarm.AlarmParam;
import com.minivision.cameraplat.rest.result.alarm.AlarmResult;

public interface MonitorRecordService {
    List<AlarmResult> findMonitorRecords(AlarmParam param);
}

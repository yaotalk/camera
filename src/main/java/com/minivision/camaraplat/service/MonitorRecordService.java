package com.minivision.camaraplat.service;

import com.minivision.camaraplat.rest.param.alarm.AlarmParam;
import com.minivision.camaraplat.rest.result.alarm.AlarmResult;

import java.util.List;

public interface MonitorRecordService {
    List<AlarmResult> findMonitorRecords(AlarmParam param);
}

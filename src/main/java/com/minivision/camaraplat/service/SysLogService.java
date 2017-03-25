package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.SysLog;

import java.util.List;

/**
 * Created by yao on 2017/3/12.
 */
public interface SysLogService {

    SysLog save(SysLog sysLog);

    List<SysLog> findAll();
}

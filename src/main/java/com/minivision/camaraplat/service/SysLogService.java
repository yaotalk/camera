package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.SysLog;
import java.util.List;

public interface SysLogService {

    SysLog save(SysLog sysLog);

    List<SysLog> findAll();
}

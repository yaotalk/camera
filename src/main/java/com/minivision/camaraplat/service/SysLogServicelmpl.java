package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.SysLog;
import com.minivision.camaraplat.repository.SysLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yao on 2017/3/12.
 */
@Service
@Transactional
public class SysLogServicelmpl implements SysLogService {

    @Autowired
    private SysLogRepository sysLogRepository;

    @Override
    public SysLog save(SysLog sysLog) {
        return sysLogRepository.save(sysLog);
    }

    public List<SysLog> findAll(){
        return sysLogRepository.findAll();
    }
}

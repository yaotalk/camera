package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.SysLog;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
public interface SysLogRepository extends PagingAndSortingRepository<SysLog,Long>{

    List<SysLog> findAll();
}

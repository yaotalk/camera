package com.minivision.cameraplat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.minivision.cameraplat.domain.SysLog;

import java.util.Date;
public interface SysLogRepository extends PagingAndSortingRepository<SysLog,Long>,JpaSpecificationExecutor<SysLog>{

   // Page<SysLog> findAllByModelNameLikeAndCreateTimeBetweenOrderByCreateTimeDesc(Pageable pageable,String ModelName,Date startTime,Date endTime);
}

package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.SysLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yao on 2017/3/12.
 */
@Repository
public interface SysLogRepository extends PagingAndSortingRepository<SysLog,Long>{

    List<SysLog> findAll();
}

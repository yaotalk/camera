package com.minivision.cameraplat.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.minivision.cameraplat.domain.record.MonitorRecord;


public interface MonitorRecordRepository extends PagingAndSortingRepository<MonitorRecord,Long>,JpaSpecificationExecutor<MonitorRecord> {
}

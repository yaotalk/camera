package com.minivision.cameraplat.repository;

import com.minivision.cameraplat.domain.Face;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.minivision.cameraplat.domain.record.MonitorRecord;

import java.util.List;


public interface MonitorRecordRepository extends PagingAndSortingRepository<MonitorRecord,Long>,JpaSpecificationExecutor<MonitorRecord> {

    List<MonitorRecord> findByFace(Face face);

    int deleteBySnapshotCameraIdAndSnapshotTimestampLessThan(Long id, long time);
}

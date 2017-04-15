package com.minivision.camaraplat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.minivision.camaraplat.domain.record.SnapshotRecord;
public interface SnapshotRecordRepository extends PagingAndSortingRepository<SnapshotRecord,Long>{
    Page<SnapshotRecord> findByCameraIdAndTimestampBetween(long cameraid,long starttime,long endtime,Pageable pageable);
}

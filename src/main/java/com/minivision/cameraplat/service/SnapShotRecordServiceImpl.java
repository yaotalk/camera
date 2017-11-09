package com.minivision.cameraplat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.minivision.cameraplat.domain.record.SnapshotRecord;
import com.minivision.cameraplat.repository.SnapshotRecordRepository;
import com.minivision.cameraplat.rest.param.alarm.SnapShotParam;
import com.minivision.cameraplat.rest.result.PageResult;
import com.minivision.cameraplat.util.ChunkRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SnapShotRecordServiceImpl implements SnapShotRecordService{

    @Autowired
    private SnapshotRecordRepository snapshotRecordRepository;


    @Override
    public PageResult<SnapshotRecord> findByTimeandCameraId(SnapShotParam param) {
        Pageable pageable = new ChunkRequest(param.getOffset(), param.getLimit());
        Page<SnapshotRecord> pages = snapshotRecordRepository.findByCameraIdAndTimestampBetween(param.getCameraId(),param.getStartTime(),param.getEndTime(),pageable);
        return new PageResult<>(pages.getTotalElements(),pages.getContent());
    }

    @Override public int deleteByCameraIdAndTimestampLessThan(Long id, long time) {
        return snapshotRecordRepository.deleteByCameraIdAndTimestampLessThan(id,time);
    }
}

package com.minivision.cameraplat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.minivision.cameraplat.domain.record.SnapshotRecord;
import com.minivision.cameraplat.domain.record.SnapshotRecord.FacePos;
import com.minivision.cameraplat.repository.SnapshotRecordRepository;
import com.minivision.cameraplat.rest.param.alarm.SnapShotParam;
import com.minivision.cameraplat.rest.result.alarm.SnapShotResult;
import com.minivision.cameraplat.rest.result.alarm.SnapShotResult.FacePostion;
import com.minivision.cameraplat.util.ChunkRequest;

import java.util.ArrayList;
import java.util.List;
@Service
public class SnapShotRecordServiceImpl implements SnapShotRecordService{

    @Autowired
    private SnapshotRecordRepository snapshotRecordRepository;


    @Override
    public List<SnapShotResult> findByTimeandCameraId(SnapShotParam param) {
        Pageable pageable = new ChunkRequest(param.getOffset(), param.getLimit());
        List<SnapShotResult> snapShotResults = new ArrayList<>();
        Page<SnapshotRecord> pages = snapshotRecordRepository.findByCameraIdAndTimestampBetween(param.getCameraId(),param.getStartTime(),param.getEndTime(),pageable);
        List<SnapshotRecord>  records = pages.getContent();
        for(SnapshotRecord snapshotRecord : records){
            String imgpath = snapshotRecord.getPhotoFileName();
            FacePos facePos = snapshotRecord.getFacePosition();
            FacePostion  facePostion =  new SnapShotResult.FacePostion(facePos.getTop(),facePos.getLeft(),facePos.getWidth(),facePos.getHeight());
            SnapShotResult snapShotResult = new SnapShotResult(snapshotRecord.getTimestamp(),"0",imgpath,pages.getTotalElements(),snapshotRecord.getConfidence(),facePostion);
            snapShotResults.add(snapShotResult);
         }
         return snapShotResults;
    }
}

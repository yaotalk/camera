package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.record.MonitorRecord;
import com.minivision.camaraplat.domain.record.SnapshotRecord;
import com.minivision.camaraplat.repository.MonitorRecordRepository;
import com.minivision.camaraplat.repository.SnapshotRecordRepository;
import com.minivision.camaraplat.rest.param.alarm.AlarmParam;
import com.minivision.camaraplat.rest.result.alarm.AlarmResult;
import com.minivision.camaraplat.rest.result.alarm.AlarmResult.FacePostion;
import com.minivision.camaraplat.util.ChunkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service @Transactional public class MonitorRecordServiceImpl implements MonitorRecordService {

    @Autowired private MonitorRecordRepository monitorRecordRepository;

    @Autowired private SnapshotRecordRepository snapshotRecordRepository;

    @Override public List<AlarmResult> findMonitorRecords(AlarmParam param) {
        List<AlarmResult> alarmResults = new ArrayList<>();
        Pageable  pageable = new ChunkRequest(param.getOffset(),param.getLimit(),new Sort(
            Sort.Direction.DESC,"snapshot.timestamp","id"));
        if ("0".equals(param.getLogType())) {
            Page<MonitorRecord> recordPage = monitorRecordRepository.findAll(
                (root, criteriaQuery, cb) -> {
                    List<Predicate> list = new ArrayList<>();
                    if (param.getSex() != null) {
                        Path<Face> facePath = root.get("face");
                        list.add(cb.like(facePath.get("sex"), "%" + param.getSex() + "%"));
                    }
                    if (param.getName() != null) {
                        Path<Face> facePath = root.get("face");
                        list.add(cb.like(facePath.get("name"), "%" + param.getName() + "%"));
                    }
                    Path<SnapshotRecord> snapPath = root.get("snapshot");
                    list.add(cb.equal(snapPath.get("cameraId"), param.getCameraId()));
                    list.add(cb.between(snapPath.get("timestamp"), param.getStartTime(),
                        param.getEndTime()));

                    Predicate[] p = new Predicate[list.size()];
                    return cb.and(list.toArray(p));
                },pageable);
            for (MonitorRecord record : recordPage.getContent()) {
                AlarmResult alarmResult = new AlarmResult();
                if(record.getFace() != null){
                    alarmResult.setUsername(record.getFace().getName());
                    alarmResult.setSex(record.getFace().getSex());
                    alarmResult.setIdCard(record.getFace().getIdCard());
                    String imgpath =record.getFace().getImgpath();
                    alarmResult.setUserImgUrl(imgpath);
                }
                if(record.getSnapshot() !=null) {
                    alarmResult.setId(record.getId());
                    alarmResult.setEmergengyTime(record.getSnapshot().getTimestamp());
                    alarmResult.setCameraId(record.getSnapshot().getCameraId());
                    alarmResult.setConfidence(record.getSnapshot().getConfidence());
                    String snappath = record.getSnapshot().getPhotoFileName();
                    SnapshotRecord.FacePos facePos = record.getSnapshot().getFacePosition();
                    FacePostion facePostion =
                        new AlarmResult.FacePostion(facePos.getTop(), facePos.getLeft(), facePos.getWidth(),
                            facePos.getHeight());
                    alarmResult.setFacePosition(facePostion);
                    alarmResult.setPanoramicUrl(snappath);
                }
                alarmResult.setLogType("0");
                alarmResult.setCount(recordPage.getTotalElements());
                alarmResults.add(alarmResult);
            }
            return alarmResults;
        } else if ("1".equals(param.getLogType())) {
                pageable = new ChunkRequest(param.getOffset(),param.getLimit(),new Sort(
                Sort.Direction.DESC,"timestamp","id"));
            Page<SnapshotRecord> snapshotRecordPage = snapshotRecordRepository
                .findByCameraIdAndTimestampBetween(param.getCameraId(), param.getStartTime(),
                    param.getEndTime(), pageable);
            List<SnapshotRecord> records = snapshotRecordPage.getContent();
            for (SnapshotRecord snapshotRecord : records) {
                String imgpath = snapshotRecord.getPhotoFileName();
                SnapshotRecord.FacePos facePos = snapshotRecord.getFacePosition();
                AlarmResult alarmResult = new AlarmResult();
                FacePostion facePostion =  new AlarmResult.FacePostion(facePos.getTop(),facePos.getLeft(),facePos.getWidth(),facePos.getHeight());
                alarmResult.setId(snapshotRecord.getId());
                alarmResult.setCameraId(snapshotRecord.getCameraId());
                alarmResult.setEmergengyTime(snapshotRecord.getTimestamp());
                alarmResult.setSex("0");
                alarmResult.setPanoramicUrl(imgpath);
                alarmResult.setConfidence(snapshotRecord.getConfidence());
                alarmResult.setFacePosition(facePostion);
                alarmResult.setLogType("1");
                alarmResult.setCount(snapshotRecordPage.getTotalElements());
                alarmResults.add(alarmResult);
            }
        }
        return alarmResults;
    }
}

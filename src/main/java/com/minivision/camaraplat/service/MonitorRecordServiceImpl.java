package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.record.MonitorRecord;
import com.minivision.camaraplat.domain.record.SnapshotRecord;
import com.minivision.camaraplat.repository.MonitorRecordRepository;
import com.minivision.camaraplat.repository.SnapshotRecordRepository;
import com.minivision.camaraplat.rest.param.alarm.AlarmParam;
import com.minivision.camaraplat.rest.result.alarm.AlarmResult;
import com.minivision.camaraplat.rest.result.alarm.AlarmResult.FacePostion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional public class MonitorRecordServiceImpl implements MonitorRecordService {

    @Autowired private MonitorRecordRepository monitorRecordRepository;

    @Autowired private SnapshotRecordRepository snapshotRecordRepository;

    @Override public List<AlarmResult> findMonitorRecords(AlarmParam param) {
        List<AlarmResult> alarmResults = new ArrayList<>();
        if ("0".equals(param.getLogType())) {
            List<MonitorRecord> records =
                monitorRecordRepository.findAll((root, criteriaQuery, cb) -> {
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
                });
            Collections.sort(records, (o1, o2) -> {
                if(o1.getSnapshot().getTimestamp() < o2.getSnapshot().getTimestamp())
                    return 1;
                else if(o1.getSnapshot().getTimestamp() > o2.getSnapshot().getTimestamp())
                    return -1;
                else return 0;
            });
            for (MonitorRecord record : records) {
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
                alarmResults.add(alarmResult);
                alarmResult.setLogType("0");
            }
            return alarmResults;
        } else if ("1".equals(param.getLogType())) {
            Page<SnapshotRecord> snapshotRecordPage = snapshotRecordRepository
                .findByCameraIdAndTimestampBetween(param.getCameraId(), param.getStartTime(),
                    param.getEndTime(), null);
            List<SnapshotRecord> records = snapshotRecordPage.getContent();
            records = records.stream().sorted(
                Comparator.comparingLong(SnapshotRecord::getTimestamp).reversed()).collect(
                Collectors.toList());
            for (SnapshotRecord snapshotRecord : records) {
                String imgpath = snapshotRecord.getPhotoFileName();
                SnapshotRecord.FacePos facePos = snapshotRecord.getFacePosition();
                AlarmResult alarmResult = new AlarmResult();
                FacePostion facePostion =  new AlarmResult.FacePostion(facePos.getTop(),facePos.getLeft(),facePos.getWidth(),facePos.getHeight());
                alarmResult.setEmergengyTime(snapshotRecord.getTimestamp());
                alarmResult.setSex("0");
                alarmResult.setPanoramicUrl(imgpath);
                alarmResult.setConfidence(snapshotRecord.getConfidence());
                alarmResult.setFacePosition(facePostion);
                alarmResults.add(alarmResult);
                alarmResult.setLogType("1");
            }
        }
        return alarmResults;
    }
}

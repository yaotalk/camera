package com.minivision.cameraplat.service;

import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.domain.record.MonitorRecord;
import com.minivision.cameraplat.domain.record.SnapshotRecord;
import com.minivision.cameraplat.repository.MonitorRecordRepository;
import com.minivision.cameraplat.rest.param.alarm.AlarmParam;
import com.minivision.cameraplat.rest.result.PageResult;
import com.minivision.cameraplat.util.ChunkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

@Service
@Transactional
public class MonitorRecordServiceImpl implements MonitorRecordService {

  @Autowired
  private MonitorRecordRepository monitorRecordRepository;

  @Override
  public PageResult<MonitorRecord> findMonitorRecords(AlarmParam param) {
    Pageable pageable = new ChunkRequest(param.getOffset(), param.getLimit());
    Page<MonitorRecord> page = monitorRecordRepository.findAll((root, criteriaQuery, cb) -> {
      Path<SnapshotRecord> snapPath = root.get("snapshot");
      Path<Face> facePath = root.get("face");
      
      Predicate time = cb.between(snapPath.get("timestamp"), param.getStartTime(), param.getEndTime());
      
      Predicate condition = cb.and(time);
          
      if(param.getCameraId() != null){
        Predicate camera = cb.equal(snapPath.get("cameraId"), param.getCameraId());
        condition = cb.and(condition, camera);
      }
      
      if (param.getSex() != null) {
        Predicate sex = cb.like(facePath.get("sex"), "%" + param.getSex() + "%");
        condition = cb.and(condition, sex);
      }
      
      if (param.getName() != null) {
        Predicate name = cb.like(facePath.get("name"), "%" + param.getName() + "%");
        condition = cb.and(condition, name);
      }
      
      if(param.getFaceToken() != null){
        Predicate faceToken = cb.equal(facePath.get("id"), param.getFaceToken());
        condition = cb.and(condition, faceToken);
      }
      criteriaQuery.where(condition);
      criteriaQuery.orderBy(cb.desc(snapPath.get("timestamp").as(long.class)));
      return criteriaQuery.getRestriction();
    }, pageable);
    
    return new PageResult<>(page.getTotalElements(), page.getContent());
  }

  @Override public int deleteBySnapshotCameraIdAndSnapshotTimestampLessThan(Long id, long time) {
    return monitorRecordRepository.deleteBySnapshotCameraIdAndSnapshotTimestampLessThan(id,time);
  }
}

package com.minivision.cameraplat.service;
import com.minivision.cameraplat.domain.FaceSet;
import com.minivision.cameraplat.param.PageParam;
import com.minivision.cameraplat.util.ChunkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minivision.cameraplat.domain.SysLog;
import com.minivision.cameraplat.repository.SysLogRepository;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Transactional
public class SysLogServicelmpl implements SysLogService {

    @Autowired
    private SysLogRepository sysLogRepository;

    @Override
    public SysLog save(SysLog sysLog) {
        return sysLogRepository.save(sysLog);
    }

    @Override public Page<SysLog> findByPage(PageParam pageParam,String ModelName,String Time1,String Time2)
        throws ParseException {
        Pageable pageable = new ChunkRequest(pageParam.getOffset(),pageParam.getLimit());
//        Date startTime = null;
//        Date endTime = null;
//        if("".equals(Time1) || Time1 == null || "".equals(Time2) || Time2 == null ){
//            startTime = new Date();
//            endTime = new Date();
//        }
//        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime    = sdf.parse(Time1);
            Date endTime = sdf.parse(Time2);
        return sysLogRepository.findAll((root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (ModelName != null && !"".equals(ModelName)) {
                Predicate name =
                    cb.like(root.get("modelName").as(String.class), "%" + ModelName + "%");
                predicates.add(name);
            }
            Predicate time = cb.between(root.get("createTime").as(Date.class),startTime,endTime);
            predicates.add(time);
            Predicate[] p = new Predicate[predicates.size()];
            return cb.and(predicates.toArray(p));
        },pageable);
     //   return sysLogRepository.findAllByModelNameLikeAndCreateTimeBetweenOrderByCreateTimeDesc(pageable,ModelName,startTime,endTime);
    }

//    public List<SysLog> findAll(){
//        return sysLogRepository.findAllByOrderByCreateTimeDesc();
//    }
}

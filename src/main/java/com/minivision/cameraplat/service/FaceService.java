package com.minivision.cameraplat.service;

import com.minivision.cameraplat.param.PageParam;
import com.minivision.cameraplat.rest.param.faceset.FacesetParam;
import org.springframework.data.domain.Page;

import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.mvc.ex.ServiceException;
import com.minivision.cameraplat.rest.param.alarm.AlarmFaceParam;
import com.minivision.cameraplat.rest.param.faceset.FaceSearchParam;
import com.minivision.cameraplat.rest.result.alarm.AlarmFacesResult;
import com.minivision.cameraplat.rest.result.faceset.FaceSearchResult;

import java.util.List;

public interface FaceService {
    void save(Face face, String facesetToken, byte[] imgData, String fileType) throws ServiceException;

    void update(Face face);

    void delete(String faceToken, String facesetToken);
    
    Face find(String faceToken);

    Page<Face> findByPage(PageParam pageParam,String facesetToken);

    Page<Face>  findByFacesetId(FacesetParam facesetParam);

    List<FaceSearchResult> searchByPlat(FaceSearchParam faceSearchParam) throws ServiceException;

    List<AlarmFacesResult> searchByFlatForAlarm(AlarmFaceParam alarmFaceParam);

}

package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.rest.param.alarm.AlarmFaceParam;
import com.minivision.camaraplat.rest.param.faceset.FaceSearchParam;
import com.minivision.camaraplat.rest.result.alarm.AlarmFacesResult;
import com.minivision.camaraplat.rest.result.faceset.FaceSearchResult;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FaceService {
    void save(Face face, String facesetToken, byte[] imgData, String fileType) throws ServiceException;

    void update(Face face);

    void delete(String faceToken, String facesetToken);
    
    Face find(String faceToken);

    Page<Face> findByFacesetId(String facesetToken,int page,int size);

    List<FaceSearchResult> searchByPlat(FaceSearchParam faceSearchParam) throws ServiceException;

    List<AlarmFacesResult> searchByFlatForAlarm(AlarmFaceParam alarmFaceParam);

}

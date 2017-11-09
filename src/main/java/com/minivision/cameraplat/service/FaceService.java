package com.minivision.cameraplat.service;

import com.minivision.cameraplat.faceplat.result.detect.faceset.RemoveFaceResult;
import com.minivision.cameraplat.param.PageParam;
import com.minivision.cameraplat.rest.param.faceset.FaceParam;
import com.minivision.cameraplat.rest.param.faceset.FaceUpdateParam;
import com.minivision.cameraplat.rest.param.faceset.FacesetParam;

import com.minivision.cameraplat.rest.result.faceset.FaceResult;
import com.minivision.cameraplat.rest.result.faceset.FaceUpdateResult;
import org.springframework.data.domain.Page;

import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.domain.FaceTmp;
import com.minivision.cameraplat.mvc.ex.ServiceException;
import com.minivision.cameraplat.rest.param.alarm.AlarmFaceParam;
import com.minivision.cameraplat.rest.param.faceset.FaceSearchParam;
import com.minivision.cameraplat.rest.result.alarm.AlarmFacesResult;
import com.minivision.cameraplat.rest.result.faceset.FaceSearchResult;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface FaceService {
    Face save(Face face, String facesetToken, byte[] imgData, String fileType)
        throws ServiceException, IOException;

    FaceResult save(FaceParam param) throws ServiceException, IOException;

    FaceUpdateResult update(FaceUpdateParam faceUpdateParam) throws ServiceException, IOException;

    void update(Face face);
        
    void save(List<FaceTmp> faceTmpList);

    RemoveFaceResult delete(String faceToken, String facesetToken)  throws ServiceException;
    
    Face find(String faceToken);

    Page<Face> findByPage(PageParam pageParam, String facesetToken);

    Page<Face>  findByFacesetId(FacesetParam facesetParam);
    
    List<Face> getRepeatFaceBySetToken(String faceSetToken, PageParam pageParam);

    List<Face> findFaceByFaceTokens(List<String> faceTokens);

   	int getRepeatFaceCoutBySetToken(String faceSetToken);

    List<FaceSearchResult> searchByPlat(FaceSearchParam faceSearchParam) throws ServiceException;

    List<AlarmFacesResult> searchByFlatForAlarm(AlarmFaceParam alarmFaceParam);

	int checkIfHasNotDetected(String faceSetToken);

	List<Face> getNotDetectedData(String faceSetToken, PageParam pageParam);
	
	List<Face> getToProcess(String taskId, PageParam pageParam, boolean ifOrderBy);
	
	int getToProcessNum(String taskId, PageParam pageParam);
	
	void deleteTaskData(String taskId);
	
	void saveOnly(List<Face> faceList);

	void saveTmp(List<FaceTmp> faceTmpList);

	List<Face> getToProcess(String taskId, PageParam pageParam,
                            boolean ifOrderBy, boolean isIncludeStore);

	boolean isCanSaveStore(FaceTmp faceTmp);

    void save(String faceToken, String token, byte[] image) throws ServiceException;

}

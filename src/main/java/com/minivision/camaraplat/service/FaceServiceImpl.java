package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Camera;
import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.domain.record.MonitorRecord;
import com.minivision.camaraplat.faceplat.client.FacePlatClient;
import com.minivision.camaraplat.faceplat.ex.FacePlatException;
import com.minivision.camaraplat.faceplat.result.FailureDetail;
import com.minivision.camaraplat.faceplat.result.detect.DetectResult;
import com.minivision.camaraplat.faceplat.result.detect.DetectedFace;
import com.minivision.camaraplat.faceplat.result.detect.SearchResult;
import com.minivision.camaraplat.faceplat.result.detect.SearchResult.Result;
import com.minivision.camaraplat.faceplat.result.detect.faceset.AddFaceResult;
import com.minivision.camaraplat.faceplat.result.detect.faceset.RemoveFaceResult;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.repository.CameraRepository;
import com.minivision.camaraplat.repository.FaceRepository;

import com.minivision.camaraplat.repository.MonitorRecordRepository;
import com.minivision.camaraplat.rest.param.alarm.AlarmFaceParam;
import com.minivision.camaraplat.rest.param.faceset.FaceSearchParam;
import com.minivision.camaraplat.rest.param.faceset.FacesetParam;
import com.minivision.camaraplat.rest.result.alarm.AlarmFacesResult;
import com.minivision.camaraplat.rest.result.faceset.FaceSearchResult;
import com.minivision.camaraplat.util.ChunkRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Transactional
@Service
public class FaceServiceImpl implements FaceService {

  private Logger logger = LoggerFactory.getLogger(FaceServiceImpl.class);
  @Autowired
  private FaceRepository faceRepository;

  @Autowired
  private FacePlatClient facePlatClient;

  @Autowired
  private MonitorRecordRepository  monitorRecordRepository;
  @Autowired
  private CameraRepository cameraRepository;

  @Value("${system.store.people}")
  private String filepath;

  @Value("${system.store.snapshot}")
  private String snapfilepath;
  /**
   * 保存人脸
   */
  @Override
  public void save(Face face, String facesetToken, byte[] imgData, String fileType) throws ServiceException{
    DetectResult detectResult = facePlatClient.detect(imgData);
    List<DetectedFace> faces = detectResult.getFaces();
    if (faces.isEmpty()) {
      throw new ServiceException("no face detected");
    }
    DetectedFace detectedFace = faces.get(0);

    String fileName = facesetToken + "/" + detectedFace.getFaceToken() + fileType;
    File imgfile = new File(filepath, fileName);
    try {
      FileUtils.writeByteArrayToFile(imgfile, imgData);
    } catch (IOException e) {
      throw new ServiceException("image file upload fail", e);
    }
    
    AddFaceResult addFaceResult = facePlatClient.addFace(facesetToken, detectedFace.getFaceToken());

    if(addFaceResult.getFacesetToken() == null){
        throw new ServiceException("add face to faceset fail");
    }
    if (addFaceResult.getFailureDetail() != null ) {
      FailureDetail failureDetail = addFaceResult.getFailureDetail().get(0);
      logger.warn("add face[{}] to faceset[{}] fail, {}", failureDetail.getFaceToken(),
          facesetToken, failureDetail.getReason());
      throw new ServiceException("add face to faceset fail, " + failureDetail.getReason());
    }
    
    face.setId(detectedFace.getFaceToken());
    face.setImgpath(fileName);
    face.setFaceSet(new FaceSet(facesetToken));
    faceRepository.save(face);
  }

  @Override
  public void update(Face face) {
       Face oldface = faceRepository.findOne(face.getId());
       face.setFaceSet(oldface.getFaceSet());
       faceRepository.save(face);
  }

  /**
   * 删除人脸
   * 
   * @param facesetToken
   * @param faceToken
   */
  public void delete(String facesetToken, String faceTokens) {
    RemoveFaceResult removeFaceResult = facePlatClient.removeFace(facesetToken,faceTokens);
    if(removeFaceResult.getFailureDetail() == null) {
      faceRepository.deleteByIdIn(Arrays.asList(faceTokens.split(",")));
    }
  }

  @Override
  public Face find(String faceToken) {
    return faceRepository.findOne(faceToken);
  }

  @Override public Page<Face> findByFacesetId(FacesetParam facesetParam) {
    Pageable pageable = new ChunkRequest(facesetParam.getOffset(), facesetParam.getLimit());
    return faceRepository.findAll((root, criteriaQuery, cb) -> {

      List<Predicate> predicates = new ArrayList<>();
      Path<FaceSet> path = root.get("faceSet");
      predicates.add(cb.like(path.get("token").as(String.class),facesetParam.getFacesetToken()));
      String search = facesetParam.getSearch();
      if(search!=null && !"".equals(search.trim())){
        Predicate name = cb.like(root.get("name").as(String.class),"%"+search+"%");
        Predicate idCard = cb.like(root.get("idCard").as(String.class),"%"+search+"%");
        Predicate phoneNumber = cb.like(root.get("phoneNumber").as(String.class),"%"+search+"%");
        Predicate sex =null;
        Predicate p4;
        if("男".equals(search)) {
          sex = cb.like(root.get("sex"), "%" + 0 + "%");
        }
        else if("女".equals(search)){
          sex = cb.like(root.get("sex"), "%" + 1 + "%");
        }
        if(sex != null) {
            p4 = cb.or(name,idCard,phoneNumber,sex);
        }
        else p4 =  cb.or(name,idCard,phoneNumber);
        predicates.add(p4);
      }
      Predicate[] p = new Predicate[predicates.size()];
      return cb.and(predicates.toArray(p));
    },pageable);
  }

  @Override
  public List<FaceSearchResult> searchByPlat(FaceSearchParam faceSearchParam) throws ServiceException {
    List<Result> results = null;
    try {
          List<FaceSearchResult> fsr = new ArrayList<>();
          List<FaceSearchResult> faceSearchResults = new ArrayList<>();
          for(String facesetToken : faceSearchParam.getFacesetTokens().split(",")){
              SearchResult searchResult = facePlatClient.search(facesetToken, faceSearchParam.getImgfile().getBytes(),
                      faceSearchParam.getLimit());
              if(searchResult.getResults() != null){
              results = searchResult.getResults();
              for(Result result : results) {
                  double confidence = result.getConfidence();
                  if ((double) faceSearchParam.getThreshold() / 100 <= confidence) {
                    String faceToken = result.getFaceToken();
                    Face face = faceRepository.findOne(faceToken);
                    FaceSearchResult fs =  new FaceSearchResult();
                    if(face!=null) {
                      fs.setFacesetToken(face.getFaceSet().getToken());
                      fs.setFaceToken(faceToken);
                      fs.setName(face.getName());
                      fs.setSex(face.getSex());
                      fs.setIdCard(face.getIdCard());
                      fs.setImgpath(face.getImgpath());
                      fs.setConfidence(confidence);
                      fsr.add(fs);
                  }
               }
           }
        }
      }
      fsr.stream().sorted(Comparator.comparing(FaceSearchResult::getConfidence).reversed()).limit(faceSearchParam.getLimit()).forEach(faceSearchResults :: add);
      return  faceSearchResults;
    }catch (IOException e){
        throw new ServiceException("File upload failed");
    }catch (FacePlatException e){
        throw  new ServiceException(e.getMessage());
    }
  }

  @Override public List<AlarmFacesResult> searchByFlatForAlarm(AlarmFaceParam alarmFaceParam) {
    MonitorRecord monitorRecord = monitorRecordRepository.findOne(alarmFaceParam.getLogid());
    String snapPath = snapfilepath + File.separator + monitorRecord.getSnapshot().getPhotoFileName();
    try {
      byte[] bytes = FileUtils.readFileToByteArray(new File(snapPath));
      Camera camera = cameraRepository.findOne(monitorRecord.getSnapshot().getCameraId());
      List<AlarmFacesResult> afrs = new ArrayList<>();
      List<AlarmFacesResult> alarmFacesResults = new ArrayList<>();
      for (FaceSet faceSet : camera.getFaceSets()) {
        List<Result> results = null;
        SearchResult searchResult = facePlatClient.search(faceSet.getToken(), bytes, alarmFaceParam.getLimit());
        if (searchResult.getResults() != null) {
          results = searchResult.getResults();
          for (Result result : results) {
            double confidence = result.getConfidence();
            String faceToken = result.getFaceToken();
            Face face = faceRepository.findOne(faceToken);
            AlarmFacesResult afr = new AlarmFacesResult();
            if (face != null) {
              afr.setId(face.getId());
              afr.setName(face.getName());
              afr.setUserImgUrl(face.getImgpath());
              afr.setConfidence(confidence);
              afrs.add(afr);
            }
          }
        }
      }
      afrs.stream().sorted(Comparator.comparing(AlarmFacesResult::getConfidence).reversed())
          .limit(alarmFaceParam.getLimit()).forEach(alarmFacesResults::add);
      return alarmFacesResults;
    } catch (IOException e) {
          e.printStackTrace();
          return null;
    }
  }
}

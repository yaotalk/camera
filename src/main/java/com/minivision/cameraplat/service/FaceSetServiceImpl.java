package com.minivision.cameraplat.service;

import com.minivision.cameraplat.faceplat.ex.FacePlatException;
import com.minivision.cameraplat.rest.param.faceset.FaceSetAddParam;
import com.minivision.cameraplat.rest.param.faceset.FaceSetUpdateParam;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.minivision.cameraplat.domain.Camera;
import com.minivision.cameraplat.domain.FaceSet;
import com.minivision.cameraplat.faceplat.client.FacePlatClient;
import com.minivision.cameraplat.faceplat.result.detect.faceset.SetCreateResult;
import com.minivision.cameraplat.faceplat.result.detect.faceset.SetDeleteResult;
import com.minivision.cameraplat.faceplat.result.detect.faceset.SetDetailResult;
import com.minivision.cameraplat.faceplat.result.detect.faceset.SetModifyResult;
import com.minivision.cameraplat.mvc.ex.ServiceException;
import com.minivision.cameraplat.repository.CameraRepository;
import com.minivision.cameraplat.repository.FaceSetRepository;

import java.io.File;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
// TODO with redis
public class FaceSetServiceImpl implements FaceSetService {

  private final FaceSetRepository faceSetRepository;

  private static final Logger log = LoggerFactory.getLogger(FaceSetServiceImpl.class);
  @Autowired
  private FacePlatClient facePlatClient;
  @Autowired
  private CameraRepository cameraRepository;

  @Value("${system.store.people}")
  private String filepath;

  public FaceSetServiceImpl(FaceSetRepository faceSetRepository) {
    this.faceSetRepository = faceSetRepository;
  }

  @Override
  public List<FaceSet> findAll() {
    List<FaceSet> faceSets = faceSetRepository.findAll();
    return faceSets;
  }

  @Override
  public List<FaceSet> findByFaceplat() {
    List<FaceSet> faceSets = faceSetRepository.findAll();

    for (FaceSet faceSet : faceSets)
      try {
        SetDetailResult setDetailResult = facePlatClient.getFaceSetDetail(faceSet.getToken());
        if (setDetailResult.getFacesetToken() != null) {
          faceSet.setFaceCount(setDetailResult.getFaceCount());
          if (faceSet.getName() == null) {
            faceSet.setName(setDetailResult.getDisplayName());
          }
        } else
          faceSet.setFaceCount(-1);
      } catch (Exception e) {
        log.error("Fail to get Detail for faceset:{}",e.getMessage());
        faceSet.setFaceCount(-1);
      }
    return faceSets;
  }

  @Override
  public FaceSet update(FaceSet faceSet) {
    Assert.notNull(faceSet, "faceSet must not be null");
    Assert.notNull(faceSet.getToken(), "faceSet must not be null");
    FaceSet oldfaceset = faceSetRepository.findOne(faceSet.getToken());
    SetModifyResult modifyResult = facePlatClient.updateFaceset(faceSet);
    if (modifyResult != null && modifyResult.getFacesetToken() != null) {
      // faceSet.setToken(modifyResult.getFacesetToken());
      faceSet.setCreateTime(oldfaceset.getCreateTime());
      return faceSetRepository.save(faceSet);
    }
    return null;
  }

  @Override
  public FaceSet update(FaceSetUpdateParam faceSetUpdateParam) throws ServiceException {
    try {
      Assert.notNull(faceSetUpdateParam.getFaceSetToken(), "faceSet must not be null");
      FaceSet oldFaceSet = faceSetRepository.findOne(faceSetUpdateParam.getFaceSetToken());
      FaceSet faceSet = new FaceSet(faceSetUpdateParam.getFaceSetToken());
      faceSet.setName(Optional.ofNullable(faceSetUpdateParam.getDisPlayName()).orElse(oldFaceSet.getName()));
      faceSet.setCapacity(faceSetUpdateParam.getCapacity()==0?oldFaceSet.getCapacity():faceSetUpdateParam.getCapacity());
      faceSet.setPriority(faceSetUpdateParam.getPriority()==0?oldFaceSet.getPriority():faceSetUpdateParam.getPriority());
      SetModifyResult modifyResult = facePlatClient.updateFaceset(faceSet);
      if (modifyResult != null && modifyResult.getFacesetToken() != null) {
        faceSet.setCreateTime(oldFaceSet.getCreateTime());
        return faceSetRepository.save(faceSet);
      }
    }
    catch (Exception e){
        log.error(e.getMessage());
        throw new ServiceException(e);
    }
    return null;
  }

  public FaceSet create(FaceSet faceSet) throws ServiceException {
    Assert.notNull(faceSet, "faceSet must not be null");
    try {
      SetCreateResult setCreateResult = facePlatClient.createFaceset(faceSet);
      if (setCreateResult != null && setCreateResult.getFacesetToken() != null) {
        faceSet.setToken(setCreateResult.getFacesetToken());
        return faceSetRepository.save(faceSet);
      }
    }catch (Exception e){
      log.error("add faceSet failed [{}]",e.getMessage());
      throw new ServiceException(e);
    }
    return null;
  }

  public FaceSet addFaceset(FaceSetAddParam faceSetAddParam) throws ServiceException {
    try {
      FaceSet faceSet = new FaceSet();
      faceSet.setName(faceSetAddParam.getDisPlayName());
      faceSet.setCapacity(faceSetAddParam.getCapacity());
      faceSet.setPriority(faceSetAddParam.getPriority());
      faceSet.setCreateTime(new Date());
      SetCreateResult setCreateResult = facePlatClient.createFaceset(faceSet);
      if (setCreateResult != null && setCreateResult.getFacesetToken() != null) {
        faceSet.setToken(setCreateResult.getFacesetToken());
        return faceSetRepository.save(faceSet);
      }
    }catch (Exception e){
      log.error("add faceSet failed {}",e.getMessage());
      throw new ServiceException(e);
    }
    return null;
  }
  @Override
  public FaceSet find(String token) {
    Assert.notNull(token, "token must not be null");
    return faceSetRepository.findOne(token);
  }

  @Override
  public void delete(String token) throws ServiceException {
    Assert.notNull(token, "token must not be null");
  try {
    List<Camera> dynamicCameras = cameraRepository.findByfaceSetToken(token);
    List<Camera> cameras = cameraRepository.findByfaceSetsToken(token);
    FaceSet faceSet = faceSetRepository.findOne(token);
    for (Camera camera : dynamicCameras) {
      camera.setFaceSet(null);
    }
    for (Camera camera : cameras) {
      camera.getFaceSets().remove(faceSet);
    }
    faceSetRepository.delete(token);
    SetDeleteResult deleteResult = facePlatClient.delFaceset(token, true);
      if (deleteResult == null || deleteResult.getFacesetToken() == null) {
        throw new ServiceException("fail to delete faceset on redis,error is "+deleteResult);
      }
    } catch (FacePlatException e) {
      if (e.getMessage().contains("NOT_EXIST")) {
        log.warn("try to delete faceset on redis but not found,so force delete now "+e.getMessage());
        //        throw new ServiceException("fail to delete faceset on redis,faceset not exist");
      }
      else
      {
        log.error("fail to delete faceset,catch faceplat exception {}",e.getMessage());
        throw new ServiceException("fail to delete faceset :"+e.getMessage());
      }
    }
    catch (Exception e){
      log.error("fail to delete faceset,catch runtime exception {}",e.getMessage());
      throw  new ServiceException("failed to delete faceset, catch runtime exception: "+e.getMessage());
    }
    FileUtils.deleteQuietly(new File(filepath + File.separator + token));
  }

  @Override
  public Set<FaceSet> findAll(String ids) {
    Iterable<FaceSet> iterable = faceSetRepository.findAll(Arrays.asList(ids.split(",")));
    Set<FaceSet> set = new HashSet<FaceSet>();
    if (iterable != null) {
      Iterator<FaceSet> iterator = iterable.iterator();
      while (iterator.hasNext()) {
        set.add((FaceSet) iterator.next());
      }
    }
    return set;
  }

  @Override
  public Page<FaceSet> findAll(int page, int size) {
    Pageable pageable = new PageRequest(page, size);
    return faceSetRepository.findAll(pageable);
  }

}

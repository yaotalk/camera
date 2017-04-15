package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.*;
import com.minivision.camaraplat.mqtt.service.AnalyserConfigService;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.repository.CameraRepository;

import com.minivision.camaraplat.rest.result.system.CameraResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class CameraServiceImpl implements CameraService {

  @Autowired
  private CameraRepository cameraRepository;
  
  @Autowired
  private AnalyserConfigService analyserConfigService;
  
  @Autowired
  private AnalyserService analyserService;
  
  private Map<Long, Integer> onlineCameras = new HashMap<>();

  @Override
  public  List<Camera> findAll(){
       return cameraRepository.findAll();
  }

  @Override
  public List<CameraShow> findAllWithStatus() {
        List<Camera> cameras = cameraRepository.findAll();
        List<CameraShow> cameraShows = new ArrayList<>();
        for(Camera  camera: cameras){
               boolean isOnline = this.isOnline(camera.getId());
               CameraShow cameraShow = new CameraShow(camera,isOnline);
               cameraShows.add(cameraShow);
        }
        return cameraShows;
  }

  @Override
  public Camera update(Camera camera) throws ServiceException{
    Assert.notNull(camera.getId(), "the camera id can not be null");
    
    Camera oldCamera = cameraRepository.findOne(camera.getId());
    
    Assert.notNull(oldCamera, "the camera not exist");
    if(oldCamera.getAnalyser().getId() != camera.getAnalyser().getId()){
      if(analyserService.isOnline(oldCamera.getAnalyser().getId())){
        analyserConfigService.asyncDeleteCamera(oldCamera);
      }
    }
    
    if(analyserService.isOnline(camera.getAnalyser().getId())){
      analyserConfigService.asyncAddOrUpdateCamera(camera);
    }
    
    return cameraRepository.save(camera);
  }

  @Override
  public Camera create(Camera camera) {
    Camera save = cameraRepository.save(camera);
    if(analyserService.isOnline(camera.getAnalyser().getId())){
      analyserConfigService.asyncAddOrUpdateCamera(save);
    }
    return save;
  }

  @Override
  public void delete(long id) {
    Camera camera = cameraRepository.findOne(id);
    if(analyserService.isOnline(camera.getAnalyser().getId())){
      analyserConfigService.asyncDeleteCamera(camera);
    }
    cameraRepository.delete(id);
  }

  @Override
  public Camera findByid(long id) {
    return cameraRepository.findOne(id);
  }

  @Override
  public Set<Camera> findByIds(List<Long> ids) {
    Set<Camera> sets = cameraRepository.findByIdIn(ids);
    return sets;
  }

  @Override
  public List<Camera> findByRegion(Region region) {
    return cameraRepository.findByRegion(region);
  }

  @Override 
  public List<Camera> findByStategy(Long strategyId) {
    Strategy strategy = new Strategy();
    strategy.setId(Long.valueOf(strategyId));
    return cameraRepository.findByStrategy(strategy);
  }

  @Override
  public boolean isOnline(long cameraId) {
    return onlineCameras.get(cameraId)!=null && onlineCameras.get(cameraId) == 1;
  }
  
  
  public void updateOnlineStatus(long cameraId, int status){
    onlineCameras.put(cameraId, status);
  }

  @Override 
  public List<CameraResult> findAllCameraResults() {
        List<Camera> cameras = cameraRepository.findAll();
        List<CameraResult> cameraResults = new ArrayList<>();
        for(Camera  camera: cameras){
          boolean isOnline = this.isOnline(camera.getId());
          CameraResult cameraResult = new CameraResult();
          cameraResult.setId(camera.getId());
          cameraResult.setDeviceName(camera.getDeviceName());
          cameraResult.setDeviceIp(camera.getIp());
          cameraResult.setUsername(camera.getUsername());
          cameraResult.setPassword(camera.getPassword());
          cameraResult.setDevicePort(camera.getPort());
          cameraResult.setRtspPort(camera.getRtspPort());
          cameraResult.setWebPort(camera.getWebPort());
          cameraResult.setAnalyserId(camera.getAnalyser() == null?null:camera.getAnalyser().getId());
          cameraResult.setRegionId(camera.getRegion() == null?null:camera.getRegion().getId());
          cameraResult.setType(camera.getType());
          cameraResult.setOnline(isOnline);
          cameraResults.add(cameraResult);
        }
        return cameraResults;
  }

  public static class CameraShow{
     private Camera camera;
     private boolean isonline;

    public CameraShow() {
    }

    public CameraShow(Camera camera, boolean isonline) {
      this.camera = camera;
      this.isonline = isonline;
    }

    public Camera getCamera() {
      return camera;
    }

    public void setCamera(Camera camera) {
      this.camera = camera;
    }

    public boolean isonline() {
      return isonline;
    }

    public void setIsonline(boolean isonline) {
      this.isonline = isonline;
    }
  }

}

package com.minivision.cameraplat.service;

import com.minivision.cameraplat.domain.Camera;
import com.minivision.cameraplat.domain.EntranceGuard;
import com.minivision.cameraplat.domain.Region;
import com.minivision.cameraplat.domain.Strategy;
import com.minivision.cameraplat.mqtt.service.AnalyserConfigService;
import com.minivision.cameraplat.mvc.ex.ServiceException;
import com.minivision.cameraplat.repository.CameraRepository;
import com.minivision.cameraplat.repository.EntranceRepository;
import com.minivision.cameraplat.rest.result.system.CameraResult;
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

  @Autowired
  private EntranceRepository entranceRepository;
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
    List<EntranceGuard.Door> doors = oldCamera.getDoors();
    camera.setDoors(doors);
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
  public void delete(Camera camera) {
    Camera old_camera = cameraRepository.findOne(camera.getId());
    if(analyserService.isOnline(old_camera.getAnalyser().getId())){
      analyserConfigService.asyncDeleteCamera(old_camera);
    }
    cameraRepository.delete(camera);
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
          cameraResult.setPadId(camera.getPadId());
          cameraResult.setIsOut(camera.getIsOut());
          if(camera.getDoors()!=null){
            List<Long> ids = new ArrayList<>();
            for(EntranceGuard.Door door : camera.getDoors()){
                ids.add(door.getId());
              }
            Set<EntranceGuard> entranceGuards = entranceRepository.findBydoorsIdIn(ids);
            ids.clear();
            for(EntranceGuard entranceGuard : entranceGuards){
               ids.add(entranceGuard.getId());
             }
            cameraResult.setEntranceGuardIds(ids);
          }
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

package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.*;
import com.minivision.camaraplat.mqtt.service.AnalyserConfigService;
import com.minivision.camaraplat.repository.CameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CameraServiceImpl implements CameraService {

  @Autowired
  private CameraRepository cameraRepository;
  
  @Autowired
  private AnalyserConfigService analyserService;

  @Override
  public List<Camera> findAll() {
    return cameraRepository.findAll();

  }

  @Override
  public Camera update(Camera camera) {
    Assert.notNull(camera.getId(), "the camera id can not be null");
    
    Camera oldCamera = cameraRepository.findOne(camera.getId());
    
    Assert.notNull(oldCamera, "the camera not exist");
    
    if(oldCamera.getAnalyser().getId() != camera.getAnalyser().getId()){
      analyserService.deleteCamera(oldCamera);
    }
    analyserService.addOrUpdateCamera(camera);
    return cameraRepository.save(camera);
  }

  @Override
  public Camera create(Camera camera) {
    Camera save = cameraRepository.save(camera);
    analyserService.addOrUpdateCamera(save);
    return save;
  }

  @Override
  public void delete(long id) {
    cameraRepository.delete(id);
  }

  @Override
  public Page<Camera> findCameras(Pageable pageable) {
    return cameraRepository.findAll(pageable);
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

  @Override public List<Camera> findByStategy(Long strategyId) {
    Strategy strategy = new Strategy();
    strategy.setId(Long.valueOf(strategyId));
    return cameraRepository.findByStrategy(strategy);
  }
}

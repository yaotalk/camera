package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Camera;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.rest.result.system.CameraResult;
import com.minivision.camaraplat.service.CameraServiceImpl.CameraShow;
import java.util.List;
import java.util.Set;

import com.minivision.camaraplat.domain.Region;

public interface CameraService {

    List<Camera> findAll();

    List<CameraShow> findAllWithStatus();

    Camera update(Camera camera) throws ServiceException;

    Camera create(Camera camera) throws ServiceException;

    void delete(long id);

    Camera findByid(long id);

    Set<Camera> findByIds(List<Long> Ids);

    List<Camera> findByRegion(Region region);

    List<Camera> findByStategy(Long strategyId);

    boolean isOnline(long cameraId);
    
    void updateOnlineStatus(long cameraId, int status);

    List<CameraResult> findAllCameraResults();
}

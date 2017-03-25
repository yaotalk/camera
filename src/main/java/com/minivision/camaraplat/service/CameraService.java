package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Camera;

import java.util.List;
import java.util.Set;

import com.minivision.camaraplat.domain.Region;
import com.minivision.camaraplat.domain.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CameraService {

    List<Camera> findAll();

    Camera update(Camera camera);

    Camera create(Camera camera);

    void delete(long id);

    Page<Camera> findCameras(Pageable pageable);

    Camera findByid(long id);

    Set<Camera> findByIds(List<Long> Ids);

    List<Camera> findByRegion(Region region);

    List<Camera> findByStategy(Long strategyId);


}

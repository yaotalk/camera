package com.minivision.cameraplat.service;

import com.minivision.cameraplat.domain.CameraGroup;

import java.util.List;

public interface CameraGroupService  {

    List<CameraGroup> findAll();

    void save(CameraGroup cameraGroup);

    void update(CameraGroup cameraGroup);

    void delete(CameraGroup cameraGroup);
}

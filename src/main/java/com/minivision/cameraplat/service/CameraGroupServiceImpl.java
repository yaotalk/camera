package com.minivision.cameraplat.service;

import com.minivision.cameraplat.domain.CameraGroup;
import com.minivision.cameraplat.repository.CameraGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraGroupServiceImpl implements CameraGroupService {

    @Autowired
    private CameraGroupRepository cameraGroupRepository;

    @Override public List<CameraGroup> findAll() {
        return cameraGroupRepository.findAll();
    }

    @Override public void save(CameraGroup cameraGroup) {
        cameraGroupRepository.save(cameraGroup);
    }

    @Override public void update(CameraGroup cameraGroup) {
        cameraGroupRepository.save(cameraGroup);
    }

    @Override public void delete(CameraGroup cameraGroup) {
        cameraGroupRepository.delete(cameraGroup);
    }
}

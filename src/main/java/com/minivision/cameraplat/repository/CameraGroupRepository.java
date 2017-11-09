package com.minivision.cameraplat.repository;

import com.minivision.cameraplat.domain.CameraGroup;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CameraGroupRepository extends PagingAndSortingRepository<CameraGroup,Long> {
    List<CameraGroup> findAll();
}

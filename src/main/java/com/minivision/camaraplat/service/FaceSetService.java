package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.FaceSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface FaceSetService {
    List<FaceSet> findAll();

    List<FaceSet> findByFaceplat();

    FaceSet update(FaceSet faceSet);

    FaceSet create(FaceSet faceSet);

    FaceSet find(String token);

    void delete(String token);

    Page<FaceSet> findFaceSets(Pageable pageable);

    Set<FaceSet> findAll(String ids);
}

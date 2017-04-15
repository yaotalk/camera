package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface FaceSetService {
    List<FaceSet> findAll();

    List<FaceSet> findByFaceplat();

    FaceSet update(FaceSet faceSet);

    FaceSet create(FaceSet faceSet);

    FaceSet find(String token);

    void delete (String token) throws ServiceException;

    Set<FaceSet> findAll(String ids);

    Page<FaceSet> findAll(int page,int size);

    List<File> getSubFile(String filepath);
}

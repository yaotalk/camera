package com.minivision.cameraplat.service;

import com.minivision.cameraplat.rest.param.faceset.FaceSetAddParam;
import com.minivision.cameraplat.rest.param.faceset.FaceSetUpdateParam;
import org.springframework.data.domain.Page;

import com.minivision.cameraplat.domain.FaceSet;
import com.minivision.cameraplat.mvc.ex.ServiceException;

import java.util.List;
import java.util.Set;

public interface FaceSetService {

    FaceSet addFaceset(FaceSetAddParam faceSetAddParam) throws ServiceException;

    List<FaceSet> findAll();

    List<FaceSet> findByFaceplat();

    FaceSet update(FaceSet faceSet);

    FaceSet update(FaceSetUpdateParam faceSetUpdateParam) throws ServiceException;

    FaceSet create(FaceSet faceSet) throws ServiceException;

    FaceSet find(String token);

    void delete (String token) throws ServiceException;

    Set<FaceSet> findAll(String ids);

    Page<FaceSet> findAll(int page,int size);

}

package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.repository.FaceSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

import javax.transaction.Transactional;

@Service
@Transactional
//TODO with redis
public class FaceSetServiceImpl implements FaceSetService {

    private final FaceSetRepository faceSetRepository;

    @Autowired
    private FacePlatService facePlatService;

    public FaceSetServiceImpl(FaceSetRepository faceSetRepository) {
        this.faceSetRepository = faceSetRepository;
    }

    @Override public List<FaceSet> findAll() {
        List<FaceSet> faceSets = faceSetRepository.findAll();
        return faceSets;
    }

    @Override public List<FaceSet> findByFaceplat() {
        List<FaceSet> faceSets = faceSetRepository.findAll();
        for(FaceSet faceSet: faceSets)
            try {
                Map map = facePlatService.getFaceSetDetail(faceSet.getToken());
                faceSet.setFaceCount((Integer)map.get("faceCount"));
                faceSet.setName(String.valueOf(map.get("displayName")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        return faceSets;
    }

    @Override public FaceSet update(FaceSet faceSet) {
        Assert.notNull(faceSet, "faceSet must not be null");
        Assert.notNull(faceSet.getToken(), "faceSet must not be null");
        return faceSetRepository.save(faceSet);
    }

    public FaceSet create(FaceSet faceSet) {
        Assert.notNull(faceSet, "faceSet must not be null");
        String  facetoen = facePlatService.createFaceset(faceSet);
        if(facetoen !=null){
            faceSet.setToken(facetoen);
            return faceSetRepository.save(faceSet);
        }
        return null;
    }

    @Override public FaceSet find(String token) {
        Assert.notNull(token, "token must not be null");
        return faceSetRepository.findOne(token);
    }

    @Override public void delete(String token) {
        Assert.notNull(token, "token must not be null");
        try {
            facePlatService.delFaceset(token,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        faceSetRepository.delete(token);
    }

    @Override public Page<FaceSet> findFaceSets(Pageable pageable) {
        return faceSetRepository.findAll(pageable);
    }

    @Override public Set<FaceSet> findAll(String ids) {
        Iterable<FaceSet> iterable = faceSetRepository.findAll(Arrays.asList(ids.split(",")));
        Set<FaceSet> set = new HashSet<FaceSet>();
        if (iterable != null) {
            Iterator<FaceSet> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                set.add((FaceSet) iterator.next());
            }
        }
        return set;
    }


}

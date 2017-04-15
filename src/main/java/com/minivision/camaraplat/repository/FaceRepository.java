package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Face;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FaceRepository extends PagingAndSortingRepository<Face, String> {
    void deleteByIdIn(List<String> ids);
    Page<Face> findByFaceSetToken(String facesetToken,Pageable pageable);
}

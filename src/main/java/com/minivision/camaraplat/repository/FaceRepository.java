package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.record.MonitorRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FaceRepository extends PagingAndSortingRepository<Face, String> ,JpaSpecificationExecutor<Face> {
    void deleteByIdIn(List<String> ids);
    Page<Face> findByFaceSetToken(String facesetToken,Specification specification,Pageable pageable);
}

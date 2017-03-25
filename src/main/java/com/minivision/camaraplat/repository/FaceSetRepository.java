package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.FaceSet;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FaceSetRepository extends PagingAndSortingRepository<FaceSet, String> {
    List<FaceSet> findAll();
}

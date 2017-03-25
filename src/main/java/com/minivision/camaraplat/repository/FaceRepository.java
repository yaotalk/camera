package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Face;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
@Repository public interface FaceRepository extends PagingAndSortingRepository<Face, String> {
    void deleteByIdIn(List<String> ids);
}

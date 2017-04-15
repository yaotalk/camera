package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Scheme;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13 0013.
 */
public interface SchemeRepository extends PagingAndSortingRepository<Scheme, Long>{
        List<Scheme> findAll();
        void deleteByIdIn(List<Long> id);
}

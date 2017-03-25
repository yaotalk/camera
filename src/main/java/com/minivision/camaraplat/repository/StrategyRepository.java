package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Scheme;
import com.minivision.camaraplat.domain.Strategy;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13 0013.
 */
public interface StrategyRepository extends PagingAndSortingRepository<Strategy, Long> {
    List<Strategy> findAll();

    List<Strategy> findByScheme(Scheme scheme);
}

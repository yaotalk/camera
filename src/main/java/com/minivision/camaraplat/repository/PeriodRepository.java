package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Scheme;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PeriodRepository extends PagingAndSortingRepository<Scheme.Period,Long> {
}

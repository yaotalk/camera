package com.minivision.cameraplat.repository;


import com.minivision.cameraplat.domain.EntranceGuard;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface EntranceRepository extends PagingAndSortingRepository<EntranceGuard,Long> {

    List<EntranceGuard> findAll();

    EntranceGuard findBySerialNumber(String serialNumber);

    Set<EntranceGuard> findBydoorsIdIn(List<Long> doorsIds);
}

package com.minivision.cameraplat.repository;

import com.minivision.cameraplat.domain.EntranceGuard;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DoorRepository extends PagingAndSortingRepository<EntranceGuard.Door,Long>{
    List<EntranceGuard.Door> findByIdIn(List<Long> ids);
    void deleteByIdIn(List<Long> ids);
}

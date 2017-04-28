package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Region;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RegionRepository extends PagingAndSortingRepository<Region, Long> {
    List<Region> findAll();

    Region findOne(long id);

    Set<Region> findByParentNodeIn(Region region);

    List<Region> findByIdNotIn(Collection<Long> ids);
}

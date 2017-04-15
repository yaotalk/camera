package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Camera;
import com.minivision.camaraplat.domain.Region;
import com.minivision.camaraplat.domain.Strategy;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
public interface CameraRepository extends PagingAndSortingRepository<Camera, Long> {
  List<Camera> findAll();

  Set<Camera> findByIdIn(Collection<Long> id);

  List<Camera> findByRegion(Region region);

  List<Camera> findByStrategy(Strategy strategy);

  List<Camera> findByfaceSetsToken(String token);
}

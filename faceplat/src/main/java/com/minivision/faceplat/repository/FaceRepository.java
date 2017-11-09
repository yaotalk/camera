package com.minivision.faceplat.repository;

import com.minivision.faceplat.entity.Face;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FaceRepository extends CrudRepository<Face, String> {
  List<Face> findAll(Iterable<String> ids);
}

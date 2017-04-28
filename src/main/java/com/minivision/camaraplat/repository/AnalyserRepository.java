package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.Analyser;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AnalyserRepository extends PagingAndSortingRepository<Analyser,Long>{
  String findPasswordByUsername(String username);
  Analyser findByUsername(String username);
  List<Analyser> findAll();
  List<String> findByname(String name, Pageable page);
}

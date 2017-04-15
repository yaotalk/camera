package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Analyser;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnalyserService {

  Iterable<Analyser> findAll();

  Analyser update(Analyser analyser);

  Analyser create(Analyser analyser);

  void delete(Long Id);

  Page<Analyser> findAnalysers(Pageable pageable);

  String findPwdByUsername(String username);

  Analyser fingByUsername(String username);

  Analyser findById(long id);

  List<Analyser> getOnlineAnalyser();

  boolean isOnline(Long analyserId);

  void online(Long analyserId);

  void offline(Long analyserId);

  List<AnalyserServiceImpl.AnalyserShow> findAllWithStatus();

}

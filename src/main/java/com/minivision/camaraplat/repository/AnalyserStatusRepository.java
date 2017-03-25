package com.minivision.camaraplat.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.minivision.camaraplat.domain.Analyser;
import com.minivision.camaraplat.domain.AnalyserStatus;

public interface AnalyserStatusRepository extends PagingAndSortingRepository<AnalyserStatus,Long>{

  List<AnalyserStatus> findByAnalyser(Analyser analyser);

  List<AnalyserStatus> findByAnalyserId(long analyserId);

  List<AnalyserStatus> findByAnalyserOrderByTimestampAsc(Analyser analyser);
}

package com.minivision.camaraplat.service;

import java.util.List;

import com.minivision.camaraplat.domain.AnalyserStatus;

public interface AnalyserStatusService {
  AnalyserStatus save(AnalyserStatus status);
  List<AnalyserStatus> findByAnalyser(long analyserId);
  AnalyserStatus lastStatus(long analyserId);
}

package com.minivision.camaraplat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.minivision.camaraplat.domain.AnalyserStatus;
import com.minivision.camaraplat.repository.AnalyserStatusRepository;

@Service
public class AnalyserStatusServiceImpl implements AnalyserStatusService{
  
  private AnalyserStatusRepository statusRepository;

  @Override
  public AnalyserStatus save(AnalyserStatus status) {
    return statusRepository.save(status);
  }

  @Override
  public List<AnalyserStatus> findByAnalyser(long analyserId) {
    return statusRepository.findByAnalyserId(analyserId);
  }

  @Override
  public AnalyserStatus lastStatus(long analyserId) {
    return null;
  }


}

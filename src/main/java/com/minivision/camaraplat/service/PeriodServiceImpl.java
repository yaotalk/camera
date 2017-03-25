package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Scheme;
import com.minivision.camaraplat.repository.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PeriodServiceImpl implements PeriodService {

  @Autowired
  private PeriodRepository periodRepository;

  @Override public void delete(Long period ) {
     periodRepository.delete(period);
  }

  @Override public void save(Scheme.Period period) {
       periodRepository.save(period);
  }
}
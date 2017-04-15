package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Scheme;
import com.minivision.camaraplat.repository.PeriodRepository;
import com.minivision.camaraplat.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SchemeServiceImpl implements SchemeService {

  @Autowired
  private SchemeRepository schemeRepository;

  @Autowired
  private PeriodRepository periodRepository;

  @Override
  public List<Scheme> findAll() {
    return schemeRepository.findAll();
  }

  @Override
  public Scheme findOne(long id) {
    return schemeRepository.findOne(id);
  }

  @Override
  public Scheme create(Scheme scheme) {
    return schemeRepository.save(scheme);
  }

  @Override
  public Scheme update(Scheme scheme) {
    return schemeRepository.save(scheme);
  }

  @Override
  public void delete(String id) {
    List<Long> ids = new ArrayList<Long>();
    for (String index : id.split(",")) {
      ids.add(Long.valueOf(index));
      Scheme scheme = schemeRepository.findOne(Long.valueOf(index));
      for (Scheme.Period period : scheme.getPeriod()) {
        periodRepository.delete(period.getId());
      }
    }
    schemeRepository.deleteByIdIn(ids);
  }


}

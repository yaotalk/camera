package com.minivision.camaraplat.repository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.minivision.camaraplat.domain.Analyser;
import com.minivision.camaraplat.domain.AnalyserStatus;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AnalyserStatusRepositoryTest {

  @Autowired
  private AnalyserStatusRepository statusRepository;
  
  @Test
  public void test(){
    List<AnalyserStatus> status = statusRepository.findByAnalyserId(1);
    
    System.out.println(status.size());
    
    Analyser analyser = new Analyser();
    analyser.setId(1l);
    
    List<AnalyserStatus> s = statusRepository.findByAnalyser(analyser);
    
    System.out.println(s.size());
  }
  
  
}

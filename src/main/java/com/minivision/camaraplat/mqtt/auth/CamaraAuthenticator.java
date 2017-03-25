package com.minivision.camaraplat.mqtt.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.minivision.camaraplat.domain.Analyser;
import com.minivision.camaraplat.service.AnalyserService;
import io.moquette.spi.security.IAuthenticator;

@Component
public class CamaraAuthenticator implements IAuthenticator {
  private final static Logger logger = LoggerFactory.getLogger(CamaraAuthenticator.class);
  
  @Autowired
  private AnalyserService analysisService;
  
  @Override
  public boolean checkValid(String clientId, String username, byte[] password) {
    Analyser server = analysisService.fingByUsername(username);
    if(server != null){
      boolean correct = server.getPassword().equals(new String(password));
      if(!correct){
        logger.debug("unknow camara server auth fail, username: {}, password: {}",username, new String(password));
      }
      return correct;
    }
    logger.debug("unknow camara server username : {}",username);
    return false;
  }

}

package com.minivision.camaraplat.mqtt;

import java.nio.ByteBuffer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MqttMessageHandler {
  
  private ObjectMapper oMapper = new ObjectMapper();
  
  public void onAnalyserStatusReport(ByteBuffer payload){
    
  }

}

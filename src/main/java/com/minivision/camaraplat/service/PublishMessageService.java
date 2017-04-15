package com.minivision.camaraplat.service;

import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.moquette.parser.proto.messages.PublishMessage;
import io.moquette.server.Server;

@Service
public class PublishMessageService {
  
  @Autowired
  private Server mqttBroker;
  
  public void sendTo(String topic, Object payload){
    PublishMessage pm = new PublishMessage();
    pm.setTopicName(topic);
    ObjectMapper oMapper = new ObjectMapper();
    byte[] bytes;
    try {
      bytes = oMapper.writeValueAsBytes(oMapper.writeValueAsBytes(payload));
      pm.setPayload(ByteBuffer.wrap(bytes));
      mqttBroker.internalPublish(pm);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

}

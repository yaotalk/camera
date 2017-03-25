package com.minivision.camaraplat.mqtt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.minivision.camaraplat.domain.Camera;
import com.minivision.camaraplat.domain.Strategy;
import com.minivision.camaraplat.mqtt.RequestFuture;
import com.minivision.camaraplat.mqtt.message.MsgAnalyserConfig;
import com.minivision.camaraplat.mqtt.message.Packet;
import com.minivision.camaraplat.mqtt.message.PacketUtils;
import com.minivision.camaraplat.mqtt.message.Packet.Head;
import com.minivision.camaraplat.mqtt.message.Packet.Head.Code;

@Component
public class AnalyserConfigService {
  
  @Autowired
  private PacketUtils packetUtils;
  
  @Autowired
  private PublishMessageTemplate publishMessageService;

  public void syncDeviceConfig(long analyserId, MsgAnalyserConfig config){
    Head head = packetUtils.buildRequestHead(Code.SYNC_DEVICE);
    Packet<MsgAnalyserConfig> packet = new Packet<MsgAnalyserConfig>(head, config);
    RequestFuture<Void> f = publishMessageService.sendRequest("/d/"+analyserId, packet, Void.class);
    f.getResponse().getBody();
  }
  
  public void addOrUpdateCamera(Camera camera){
    MsgAnalyserConfig config = new MsgAnalyserConfig(camera);
    Head head = packetUtils.buildRequestHead(Code.UPDATE_CAMERA);
    Packet<MsgAnalyserConfig> packet = new Packet<MsgAnalyserConfig>(head, config);
    RequestFuture<Void> f = publishMessageService.sendRequest("/d/"+camera.getAnalyser().getId(), packet, Void.class);
    f.getResponse().getBody();
  }
  
  public void deleteCamera(Camera camera){
    Head head = packetUtils.buildRequestHead(Code.DEL_CAMERA);
    List<Long> ids = new ArrayList<>();
    ids.add(camera.getId());
    Map<String, List<Long>> body = new HashMap<>();
    body.put("id", ids);
    Packet<Map<String, List<Long>>> packet = new Packet<>(head, body);
    RequestFuture<Void> f = publishMessageService.sendRequest("/d/"+camera.getAnalyser().getId(), packet, Void.class, false);
    f.getResponse().getBody();
  }
  
  public void sendCameraStrategy(long analyserId, List<Strategy> strategy){
    Head head = packetUtils.buildRequestHead(Code.STRATEGY_INFO);
    Packet<List<Strategy>> packet = new Packet<>(head, strategy);
    RequestFuture<Void> f = publishMessageService.sendRequest("/d/"+analyserId, packet, Void.class);
    f.getResponse().getBody();
  }
  
  
}
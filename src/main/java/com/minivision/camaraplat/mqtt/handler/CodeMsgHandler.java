package com.minivision.camaraplat.mqtt.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.minivision.camaraplat.domain.Analyser;
import com.minivision.camaraplat.domain.AnalyserStatus;
import com.minivision.camaraplat.domain.Strategy;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.CodeHandler;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.MqttMessageBody;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.MqttMessageParam;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.MqttMessageParam.ParamType;
import com.minivision.camaraplat.mqtt.message.MsgAnalyserConfig;
import com.minivision.camaraplat.mqtt.message.MsgCameraStrategy;
import com.minivision.camaraplat.mqtt.message.Packet.Head.Code;
import com.minivision.camaraplat.mqtt.message.Packet.Head.Type;
import com.minivision.camaraplat.service.AnalyserService;
import com.minivision.camaraplat.service.AnalyserStatusService;
import com.minivision.camaraplat.service.StrategyService;

@MqttMessageHandler
public class CodeMsgHandler {
  
  @Autowired
  private AnalyserStatusService statusService;
  
  @Autowired
  private AnalyserService analyserService;
  
  @Autowired
  private StrategyService strategyService;
  
  @CodeHandler(Code.STATUS_INFO)
  public void statusReport(@MqttMessageBody AnalyserStatus status){
    statusService.save(status);
  }
  
  @CodeHandler(code = Code.SYNC_DEVICE, type = Type.REQUEST)
  public MsgAnalyserConfig getDeviceInfo(@MqttMessageParam(ParamType.username) String username){
    Analyser analyser = analyserService.fingByUsername(username);
    MsgAnalyserConfig config = new MsgAnalyserConfig(analyser, analyser.getCameras());
    return config;
  }
  
  @CodeHandler(code = Code.STRATEGY_INFO, type = Type.REQUEST)
  public List<MsgCameraStrategy> getStratefyInfo(){
    List<Strategy> strategies = strategyService.findAll();
    return strategies.stream().map(s-> new MsgCameraStrategy(s)).collect(Collectors.toList());
  }
  
}

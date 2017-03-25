package com.minivision.camaraplat.mqtt.handler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.StandardMethodMetadata;

import com.fasterxml.jackson.core.JsonParser;
import com.minivision.camaraplat.domain.MonitorImage;
import com.minivision.camaraplat.mqtt.MessageContext;
import com.minivision.camaraplat.mqtt.RequestFuture;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.CodeHandler;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.MqttMessageImage;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.MqttMessageParam;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.MqttMessageParam.ParamType;
import com.minivision.camaraplat.mqtt.handler.MqttMessageHandler.TopicHandler;
import com.minivision.camaraplat.mqtt.message.Packet;
import com.minivision.camaraplat.mqtt.message.Packet.Head;
import com.minivision.camaraplat.mqtt.message.Packet.Head.Type;
import com.minivision.camaraplat.mqtt.message.PacketUtils;
import com.minivision.camaraplat.mqtt.service.PublishMessageTemplate;

@MqttMessageHandler
public class TopicMsgDeliver {

  @Value("${system.photo.store}")
  private String imageFilePath = ".";

  private String imageFileType = "jpg";
  
  @Autowired
  private PacketUtils packetUtils;
  
  @Autowired
  private ApplicationContext context;
  
  @Autowired
  private MessageContext messageContext;
  
  @Autowired
  private PublishMessageTemplate publishMessageService;
  
  private Map<Integer, Map<Integer, MsgHandlerMethodContext>> msgHandlers = new HashMap<>();
  
  private static final Logger logger = LoggerFactory.getLogger(TopicMsgDeliver.class);
  
  
  @PostConstruct
  private void init(){
    
    Map<String, Object> beans = context.getBeansWithAnnotation(MqttMessageHandler.class);

    for (Object instance : beans.values()) {
      StandardAnnotationMetadata s = new StandardAnnotationMetadata(instance.getClass());
      Set<MethodMetadata> methods = s.getAnnotatedMethods(CodeHandler.class.getName());
      for (MethodMetadata t : methods) {
        StandardMethodMetadata m = (StandardMethodMetadata) t;
        Map<String, Object> methodMeta = m.getAnnotationAttributes(CodeHandler.class.getName());
        int[] codes = (int[]) methodMeta.get("code");
        for(int code: codes){
          Map<Integer, MsgHandlerMethodContext> codeMap = msgHandlers.get(code);
          if(codeMap == null){
            codeMap = new HashMap<>();
            msgHandlers.put(code, codeMap);
          }
          int[] types = (int[]) methodMeta.get("type");
          Method h = m.getIntrospectedMethod();
          h.setAccessible(true);
          logger.info("Found data code handler method: {}, meta={}", h, methodMeta);
          MsgHandlerMethodContext context = new MsgHandlerMethodContext(instance, h);
          if(types.length == 0){
            codeMap.put(null, context);
          }else{
            for(int type: types){
              codeMap.put(type, context);
            }
          }
        }
        
      }
    }
  }
  
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void handleGenericResponse(Head h, JsonParser p) throws IOException {
    RequestFuture<?> req = messageContext.remove(h.getId());
    if (req == null) {
        logger.error("No request found for a response, maybe timeout, discard it. Head : {}", h);
        return;
    }
    try {
        Object body = packetUtils.parseBody(p, req.getResponseBodyType());
        Packet response = new Packet<>(h, body);
        req.setResponse(response);
    } catch (Exception e) {
        logger.error("parseBody error，head: {}", h, e);
        req.fail(e);
    }
  }
  
  private void responseBadRequest(String topic, Head h){
    Packet<?> response = new Packet<Object>(packetUtils.buildResponseHead(h, Type.RESPONSE_BAD_REQ));
    publishMessageService.sendTo(topic, response);
  }

  @TopicHandler("/s")
  public void statusReport(@MqttMessageParam(ParamType.clientId) String clientId,
      @MqttMessageParam(ParamType.username) String username,
      ByteBuffer payload) {

    try {
      JsonParser parser = packetUtils.createParser(payload.array());
      Head head = packetUtils.parseHead(parser);
      int code = head.getCode();
      int type = head.getType();
      
      if(Type.isResponse(type)){
        handleGenericResponse(head, parser);
        return;
      }
      
      MsgHandlerMethodContext methodContext = null;
      
      Map<Integer, MsgHandlerMethodContext> codeMap = msgHandlers.get(code);
      if(codeMap != null){
        methodContext = codeMap.get(type);
        if(methodContext == null){
          methodContext = codeMap.get(null);
        }
      }
      if(methodContext == null){
        if(type == Type.REQUEST){
          responseBadRequest("/d/"+clientId, head);
        }
        return;
      }
      
      try {
        Packet<?> res = methodContext.process(clientId, username, head, parser);
        if(type == Type.REQUEST){
          publishMessageService.sendTo("/d/"+clientId, res);
        }
      } catch (Exception e) {
        e.printStackTrace();
        if(type == Type.REQUEST){
          responseBadRequest("/d/"+clientId, head);
        }
      }
      
    } catch (IOException e) {
     
      e.printStackTrace();
    }
  }
  
  

  @TopicHandler("/s/i")
  public void imageReport(@MqttMessageParam(ParamType.clientId) String clientId,
      @MqttMessageParam(ParamType.username) String username, @MqttMessageImage MonitorImage image) {
    logger.trace("receive an image, clientId:{}, username:{}, cameraId:{}, serialNo:{}", clientId,
        username, image.getCameraId(), image.getSerialNo());

    String fileName = image.getCameraId() + "_" + image.getSerialNo() + "." + imageFileType;
    File imgFile = new File(imageFilePath + "/" + username, fileName);
    try {
      FileUtils.touch(imgFile);
      FileUtils.writeByteArrayToFile(imgFile, image.getImage());
    } catch (IOException e) {
      e.printStackTrace();
    }
    //TODO FaceDetect
  }

}

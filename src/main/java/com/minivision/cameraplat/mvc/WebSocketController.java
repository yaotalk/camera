package com.minivision.cameraplat.mvc;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minivision.cameraplat.domain.record.SnapshotRecord;
import com.minivision.cameraplat.repository.SnapshotRecordRepository;

@RestController
public class WebSocketController {
  
  @Autowired
  private SnapshotRecordRepository snapshotRecordRepository;
  
  @Autowired
  private SimpMessagingTemplate template;

  @RequestMapping("/send")
  public void test() {
    Random random = new Random();
    SnapshotRecord record = snapshotRecordRepository.findOne(random.nextInt(120)+1l);
    template.convertAndSend("/c/snapshot", record);
  }

  @MessageMapping("/heartBeat")
  @SendTo("/c/snapshot")
  public String heartCheck(){
    //template.convertAndSend("/app/heartBeat","server is alive: "+System.currentTimeMillis());
    return "server is alive: "+System.currentTimeMillis();
  }
}

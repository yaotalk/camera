package com.minivision.camaraplat.mqtt.task;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.service.FaceService;

//只允许单任务
@Component
public class BatchTaskContext {
  
  //private Map<String, BatchTask> taskMap = new HashMap<>();
  
  private BatchTask currentTask;
  
  @Autowired
  private FaceService faceService;
  
  @Autowired
  private SimpMessagingTemplate messageTemplate;
  
  @Async
  public void submitTask(BatchTask task){
    synchronized(this){
      if(currentTask!=null && currentTask.getStatus() != BatchTask.DONE){
        throw new IllegalArgumentException("task conflict!");
      }
      currentTask = task;
    }
    task.run();
  }
  
  public BatchTask getCurrentTask(){
    return currentTask;
  }
  
  
  public BatchRegistTask createBatchRegistTask(FaceSet faceSet, String username, File path){
    BatchRegistTask task = new BatchRegistTask(faceSet.getToken(), username ,path, faceSet, this);
    return task;
  }

  public FaceService getFaceService() {
    return faceService;
  }

  public void setFaceService(FaceService faceService) {
    this.faceService = faceService;
  }
  
  public void sendLog(String taskId, String log){
    messageTemplate.convertAndSend("/w/tasklog/"+taskId, log);
  }
  
  public void sendStatus(String taskId, BatchTask task){
    messageTemplate.convertAndSend("/w/task/"+taskId, task);
  }
  
}

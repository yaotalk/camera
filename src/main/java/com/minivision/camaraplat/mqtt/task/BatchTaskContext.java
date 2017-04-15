package com.minivision.camaraplat.mqtt.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class BatchTaskContext {
  
  private Map<String, BatchTask> taskMap = new HashMap<>();
  
  public void doTask(BatchTask task){
    if(taskMap.get(task.getTaskId()) != null){
      throw new IllegalArgumentException("task conflict!");
    }
    taskMap.put(task.getTaskId(), task);
    task.run();
    //taskMap.remove(task.getTaskId());
  }
  
  public int getCurrentTaskCount(){
    return taskMap.size();
  }
  
  public BatchTask getTask(String taskId){
    return taskMap.get(taskId);
  }
  
  public BatchTask removeTask(String taskId){
    return taskMap.remove(taskId);
  }
  
}

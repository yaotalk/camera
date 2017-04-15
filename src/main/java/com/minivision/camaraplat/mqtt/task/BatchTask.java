package com.minivision.camaraplat.mqtt.task;

import java.time.Instant;

import org.springframework.scheduling.annotation.Async;

public abstract class BatchTask {
  private String taskId;
  private Instant createTime;
  private int total;
  private int progress;
  private int status;
  
  public static final int PREPARED = 0;
  public static final int RUNNING = 1;
  public static final int DONE = 2;
  
  public String getTaskId() {
    return taskId;
  }
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }
  public Instant getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Instant createTime) {
    this.createTime = createTime;
  }
  public int getTotal() {
    return total;
  }
  public void setTotal(int total) {
    this.total = total;
  }
  public int getProgress() {
    return progress;
  }
  public void setProgress(int progress) {
    this.progress = progress;
  }
  public int getStatus() {
    return status;
  }
  public void setStatus(int status) {
    this.status = status;
  }
  
  public void progress(){
    this.progress++;
  }
  
  protected abstract void doTask();
  
  @Async
  public void run(){
    this.status = RUNNING;
    doTask();
    this.status = DONE;
  }
  
}

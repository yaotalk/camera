package com.minivision.camaraplat.task;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.minivision.camaraplat.App;
import com.minivision.camaraplat.mqtt.task.BatchRegistTask;
import com.minivision.camaraplat.mqtt.task.BatchTaskContext;
import com.minivision.camaraplat.service.FaceService;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class TaskTest {
  
  @Autowired
  private BatchTaskContext taskContext;
  
  @Autowired
  private FaceService faceService;

  @Test
  public void test() throws InterruptedException{
    
    BatchRegistTask task = new BatchRegistTask(new File("E://"),"test",faceService);
    
    taskContext.doTask(task);
    
    System.out.println("kkkkkkk");
    
    Thread.sleep(1000*1000);
  }
  
  public static void main(String[] args) {
    File[] roots = File.listRoots();
    
    System.out.println(Arrays.asList(roots));
    
    File[] cfile = roots[2].listFiles(f -> f.isDirectory());
    
    System.out.println(Arrays.asList(cfile));
  }
  
}

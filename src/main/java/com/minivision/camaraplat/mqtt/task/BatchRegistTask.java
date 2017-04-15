package com.minivision.camaraplat.mqtt.task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.service.FaceService;


public class BatchRegistTask extends BatchTask{

  private static final String[] IMAGE_EXTENSION = new String[]{"jpg","jpeg","png"};
  
  private List<File> photos;
  
  private String facesetToken;
  
  private FaceService faceService;
  
  private int success;
  
  private int error;
  
  public BatchRegistTask(File dir, String facesetToken, FaceService faceService) {
    this.photos = (List<File>) FileUtils.listFiles(dir, IMAGE_EXTENSION, false);
    this.facesetToken = facesetToken;
    setTotal(photos.size());
    this.faceService = faceService;
  }
  
  @Override
  protected void doTask() {
    for(File file : photos){
      System.out.println(file.getName());
      String name = file.getName().split(".")[0];
      Face face = new Face();
      face.setName(name);
      String fileType = file.getName().substring(file.getName().lastIndexOf("."));
      try {
        byte[] bs = FileUtils.readFileToByteArray(file);
        faceService.save(face, facesetToken, bs, fileType);
        this.success++;
      } catch (IOException | ServiceException e) {
        this.error++;
        e.printStackTrace();
      }
      progress();
    }
  }

  public int getSuccess() {
    return success;
  }

  public void setSuccess(int success) {
    this.success = success;
  }

  public int getError() {
    return error;
  }

  public void setError(int error) {
    this.error = error;
  }
  
}

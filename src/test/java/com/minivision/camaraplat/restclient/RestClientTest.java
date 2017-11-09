package com.minivision.camaraplat.restclient;

import java.io.File;
import java.io.IOException;

import com.minivision.cameraplat.App;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
//import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.minivision.cameraplat.faceplat.client.RestClient;

@SuppressWarnings("deprecation")
@SpringBootTest(classes=App.class)
public class RestClientTest {
//  @Autowired
  private RestClient client = new RestClient();
  
  @Test
  public void testRestClient() throws IOException{
    String url = "http://localhost:8080/api/v1/addFace";
    MultiValueMap<String, Object> mmap = new LinkedMultiValueMap<>();
    byte[] byteArray = FileUtils.readFileToByteArray(new File("E://b.jpg"));
    
    ByteArrayResource arrayResource = new ByteArrayResource(byteArray){ 
      @Override
      public String getFilename() throws IllegalStateException { 
          return "temp.jpg";
      }
    }; 
    
    mmap.add("imageFile", arrayResource);
//    mmap.add("apiKey", "test");
//    mmap.add("apiSecret", "test");
    String form = client.post(url, mmap);
    System.err.println(form);
  }
}

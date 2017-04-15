package com.minivision.camaraplat.restclient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.minivision.camaraplat.faceplat.client.FacePlatClient;
import com.minivision.camaraplat.faceplat.client.RestClient;
import com.minivision.camaraplat.faceplat.result.detect.CompareResult;

@RunWith(SpringRunner.class)
@RestClientTest({FacePlatClient.class,RestClient.class})
public class FacePlatTest {

  @Autowired
  private FacePlatClient client;
  
  @Test
  public void test(){
    
    //byte[] image = FileUtils.readFileToByteArray(new File("E://44.jpg"));
    //SearchResult search = client.search("d4f8f338-a751-4875-b044-a521712f5b03", image, 100);
    //CompareResult result = client.compare("c52c68a5-23c2-4ac8-b07d-8b1b4e5347e91", "f7426a29-bd7b-450d-ad5c-637da4cf94b7");
    CompareResult result = client.compare("c52c68a5-23c2-4ac8-b07d-8b1b4e5347e9", "f7426a29-bd7b-450d-ad5c-637da4cf94b7");
    System.out.println(ToStringBuilder.reflectionToString(result));
  }
}

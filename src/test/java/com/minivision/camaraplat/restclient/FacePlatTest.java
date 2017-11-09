package com.minivision.camaraplat.restclient;

import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.domain.record.MonitorRecord;
import com.minivision.cameraplat.repository.MonitorRecordRepository;
import com.minivision.cameraplat.service.FaceService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.minivision.cameraplat.App;
import com.minivision.cameraplat.faceplat.client.FacePlatClient;
import com.minivision.cameraplat.faceplat.result.detect.faceset.SetListResult;

import java.util.List;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class)
public class FacePlatTest {

  @Autowired
  private FacePlatClient client;

  @Autowired
  private MonitorRecordRepository monitorRecordRepository;

  @Autowired
  private FaceService faceService;
  @Test
  public void test(){
    
    //byte[] image = FileUtils.readFileToByteArray(new File("E://44.jpg"));
    //SearchResult search = client.search("d4f8f338-a751-4875-b044-a521712f5b03", image, 100);
    //CompareResult result = client.compare("c52c68a5-23c2-4ac8-b07d-8b1b4e5347e91", "f7426a29-bd7b-450d-ad5c-637da4cf94b7");
    SetListResult faceList = client.faceList(0, 2);
    System.out.println(ToStringBuilder.reflectionToString(faceList));
  }

  @Test
  public void test2(){
//         Face face = new Face();
//         face.setId("b609f86c-16f8-4395-88d5-1f94142c029e");
        Face face = faceService.find("79c1512d-6832-40d0-9714-b5fe40769b9a");
         List<MonitorRecord>  monitorRecords =  monitorRecordRepository.findByFace(face);
         monitorRecords.forEach(new Consumer<MonitorRecord>() {
           @Override public void accept(MonitorRecord monitorRecord) {
             System.out.println(monitorRecord.getFace());
           }
         });
    }
}

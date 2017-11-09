package com.minivision.faceplat.redis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.minivision.faceplat.entity.FaceSet;
import com.minivision.faceplat.repository.FaceSetRepository;
import com.minivision.faceplat.rest.result.detect.DetectedFace;
import com.minivision.faceplat.rest.result.faceset.AddFaceResult;
import com.minivision.faceplat.rest.result.faceset.SetCreateResult;
import com.minivision.faceplat.rest.result.faceset.SetDetailResult;
import com.minivision.faceplat.service.FaceCommonService;
import com.minivision.faceplat.service.FaceService;
import com.minivision.faceplat.service.FaceSetService;
import com.minivision.faceplat.util.ImageUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisRepositoryTest {

  @Autowired
  private FaceService faceService;

  @Autowired
  private FaceSetService faceSetService;
  
  @Autowired
  private FaceCommonService commonService;
  
  @Autowired
  private StringRedisTemplate stringRedisTemplate;
  
  @Autowired
  private FaceSetRepository setRepository;

  // @Test
  public void test() throws Exception {

    // 创建人脸库
    String owner = "test", outerId = "test", displayName = "test";
    SetCreateResult setCreateResult = faceSetService.create(owner, outerId, displayName);
    System.err.println(setCreateResult.getFacesetToken());
    Assert.assertTrue(setCreateResult != null);
    String setToken = setCreateResult.getFacesetToken();

    // 检测人脸
    File file = new File("D:/face1/");
    File[] listFiles = file.listFiles();
    for (File tempFile : listFiles) {

      BufferedImage bufferedImage = ImageIO.read(tempFile);
      BufferedImage image = ImageUtils.resize(bufferedImage);
      byte[] imagedataBytes = ImageUtils.writeImageToBytes(image, "png");
      List<DetectedFace> detect = faceService.detect(imagedataBytes);
      Assert.assertTrue(detect.size() > 0);

      // 添加人脸
      String[] faceTokens = new String[detect.size()];
      for (int i = 0; i < detect.size(); i++) {
        DetectedFace temp = detect.get(i);
        System.err.println(tempFile.getName() + "---" + temp.getFaceToken());
        faceTokens[i] = temp.getFaceToken();
      }

      AddFaceResult addFaceResult = faceSetService.addFace(setToken, faceTokens);
      Assert.assertTrue(addFaceResult.getFaceAdded() == detect.size());

    }

    // 查看人脸库信息
    SetDetailResult faceSetDetail = faceSetService.getFaceSetDetail(setToken, 0, 10);
    System.err.println(faceSetDetail.getFaceTokens());
  }

  @Test
  public void testFaceSet(){
    List<FaceSet> all = (List<FaceSet>) setRepository.findAll();
    PageRequest pageRequest = new PageRequest(1, 2);
    Page<FaceSet> page = setRepository.findAll(pageRequest);
    System.out.println(all);
    System.out.println(page.getContent());
  }
  
  //@Test
  public void testFaceList(){
    System.err.println("start");
    Set<String> faceTokens = stringRedisTemplate.opsForZSet()
        .rangeByScore(commonService.getFaceSetKey("3cb26872-700a-4d68-9933-7b5e31ca2cb5"), 0, 0);
    
    System.err.println(faceTokens.size());
    
    long start = System.currentTimeMillis();
    List<float[]> features = commonService.getAllFeatures(faceTokens);
    System.err.println(System.currentTimeMillis()-start +" ms");
    System.err.println(features.size());
  }

}

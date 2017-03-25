package com.minivision.faceplat.redis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.minivision.faceplat.rest.result.DetectedFace;
import com.minivision.faceplat.rest.result.detect.SearchResult;
import com.minivision.faceplat.rest.result.detect.SearchResult.Result;
import com.minivision.faceplat.rest.result.faceset.AddFaceResult;
import com.minivision.faceplat.rest.result.faceset.SetCreateResult;
import com.minivision.faceplat.rest.result.faceset.SetDetailResult;
import com.minivision.faceplat.service.FaceService;
import com.minivision.faceplat.service.FaceSetService;
import com.minivision.faceplat.util.BufferedImage2ByteArray;
import com.minivision.faceplat.util.ResizeImg;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisRepositoryTest {

	@Autowired
	private FaceService faceService;

	@Autowired
	private FaceSetService faceSetService;

	//@Test
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
			BufferedImage image = ResizeImg.resizeImage(bufferedImage);
			byte[] imagedataBytes = BufferedImage2ByteArray.bufferedImage2ByteArray(image);
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
	public void search() throws Exception {
		SearchResult searchResult = faceService.search("fdd27626-2d92-4841-82c9-d315eab84cad", null,
				"03b4f9a2-9f20-4964-8be0-dfe55a0ff4a4", 5);
		List<Result> results = searchResult.getResults();
		for (Result temp : results) {
			System.err.println(temp.getFaceToken() + "--" + temp.getConfidence());
		}

	}

}

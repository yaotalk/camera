package com.minivision.faceplat.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.minivision.faceplat.entity.Face;
import com.minivision.faceplat.pool.ThriftServiceClientProxyFactory;
import com.minivision.faceplat.rest.result.DetectedFace;
import com.minivision.faceplat.rest.result.DetectedFace.Rectangle;
import com.minivision.faceplat.rest.result.detect.CompareResult;
import com.minivision.faceplat.rest.result.detect.SearchResult;
import com.minivision.faceplat.rest.result.detect.SearchResult.Result;
import com.minivision.faceplat.service.ex.ErrorType;
import com.minivision.faceplat.service.ex.FacePlatException;
import com.minivision.faceplat.thrift.ReIDFeatures;
import com.minivision.faceplat.thrift.Serv.Iface;
import com.minivision.faceplat.thrift.ThriftImage;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Transactional
@Service
@ConfigurationProperties("algorithm.param")
public class FaceServiceThrift implements FaceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FaceServiceThrift.class);

	@Autowired
	private Cache faceCache;

	@Autowired
	private ThriftServiceClientProxyFactory imageService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private FaceCommonService commonService;

	private double[] scoreLevels;

	public double[] getScoreLevels() {
		return scoreLevels;
	}

	public void setScoreLevels(double[] scoreLevels) {
		this.scoreLevels = scoreLevels;
	}

	/**
	 * 人脸检测
	 */
	@Override
	public List<DetectedFace> detect(byte[] img) throws FacePlatException {

		LOGGER.info("face detect at service");

		Iface client = (Iface) imageService.getObject();
		ThriftImage tr = new ThriftImage();
		tr.setData(img);

		try {
			BufferedImage bi = Imaging.getBufferedImage(img);
			List<ReIDFeatures> reIDFeatures = client.GetFeatures(tr);
			List<DetectedFace> result = new ArrayList<>();
			for (ReIDFeatures reIDFeature : reIDFeatures) {
				DetectedFace detectedFace = new DetectedFace();
				Rectangle rectangle = detectedFace.new Rectangle();
				List<Integer> bbox = reIDFeature.getBbox();
				assert bbox.size() == 4;
				rectangle.setLeft(bbox.get(0));
				rectangle.setTop(bbox.get(1));
				rectangle.setWidth(bbox.get(2));
				rectangle.setHeight(bbox.get(3));
				detectedFace.setFaceRectangle(rectangle);

				List<Double> features = reIDFeature.getFeatures();
				double[] featureArray = new double[features.size()];
				for (int i = 0; i < features.size(); i++) {
					featureArray[i] = features.get(i).doubleValue();
				}
				detectedFace.setFeature(featureArray);

				Face face = new Face();
				face.setFuture(featureArray);
				face.setToken(UUID.randomUUID().toString());

				BufferedImage subimage = bi.getSubimage(rectangle.getLeft(), rectangle.getTop(), rectangle.getWidth(),
						rectangle.getHeight());
				//This image format (Jpeg-Custom) cannot be written.
				face.setImg(Imaging.writeImageToBytes(subimage, ImageFormats.PNG, null));
				
				// save in cache ,not redis
				// faceRepository.save(face);
				faceCache.putIfAbsent(new Element(face.getToken(), face));
				detectedFace.setFaceToken(face.getToken());

				result.add(detectedFace);
			}
			return result;
		} catch (TException e) {
			throw new FacePlatException(ErrorType.FACE_ALGO_ERROR, e);
		} catch (IOException e) {
			throw new FacePlatException(ErrorType.ARGUMENT_ERROR, e);
		} catch (ImageReadException e) {
			throw new FacePlatException(ErrorType.ARGUMENT_ERROR, e);
		} catch (ImageWriteException e) {
			throw new FacePlatException(ErrorType.ARGUMENT_ERROR, e);
		}

	}

	@Override
	public CompareResult compare(String faceToken1, String faceToken2, byte[] img1, byte[] img2)
			throws FacePlatException {
		return null;
	}

	@Override
	public SearchResult search(String faceToken, byte[] img, String facesetToken, int count) throws FacePlatException {

		SearchResult searchResult = new SearchResult();
		List<Result> results = new ArrayList<>();

		double[] feature = {};
		if (!StringUtils.isEmpty(faceToken)) {
			Face face = commonService.findOneFace(faceToken);
			feature = face.getFuture();
		} else {
			List<DetectedFace> detectedFaceList = detect(img);
			// 第一个人脸进行人脸搜索
			DetectedFace detectedFace = detectedFaceList.get(0);
			feature = detectedFace.getFeature();
			faceToken = detectedFace.getFaceToken();
			searchResult.setFaces(detectedFaceList);
		}

		Set<String> faceTokens = stringRedisTemplate.opsForZSet()
				.rangeByScore(commonService.getFaceSetKey(facesetToken), 0, 0);
		for (String token : faceTokens) {
			Face face = commonService.findOneFace(token);
			double dist = calDist(feature, face.getFuture());

			Result result = searchResult.new Result();
			result.setFaceToken(token);
			result.setConfidence(getScore(dist));

			results.add(result);
		}

		results = results.stream().sorted((e1, e2) -> (e1.getConfidence() < e2.getConfidence() ? 1 : -1)).limit(count)
				.collect(Collectors.toList());
		searchResult.setResults(results);
		searchResult.setFaceToken(faceToken);
		return searchResult;
	}

	// 计算N维向量的欧式距离
	private double calDist(double[] faceFutureArray1, double[] faceFutureArray2) {

		double confidence = 0;
		for (int i = 0; i < faceFutureArray1.length; i++) {
			double faceFuture1 = faceFutureArray1[i];
			double faceFuture2 = faceFutureArray2[i];
			confidence += Math.pow(faceFuture1 - faceFuture2, 2);
		}
		return Math.sqrt(confidence);
	}

	private double getScore(double dist) {
		float score = 0;
		if (dist < scoreLevels[0]) {
			score = (float) ((scoreLevels[0] - dist) / scoreLevels[0] * 0.2 + 0.8);
		} else if ((dist >= scoreLevels[0]) && (dist <= scoreLevels[1])) {
			score = (float) (0.8 - (dist - scoreLevels[0]) / ((scoreLevels[1] - scoreLevels[0])) * 0.2);
		} else if ((dist > scoreLevels[1]) && (dist < scoreLevels[2])) {
			score = (float) ((scoreLevels[2] - dist) / (scoreLevels[2] - scoreLevels[1]) * 0.6);
		} else if (dist > scoreLevels[2]) {
			score = 0;
		}
		return (score <= 1.0 ? score : 1.0);
	}

}

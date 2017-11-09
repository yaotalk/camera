package com.minivision.faceplat.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.minivision.faceplat.entity.Face;
import com.minivision.faceplat.entity.FaceSet;
import com.minivision.faceplat.repository.FaceSetRepository;
import com.minivision.faceplat.service.ex.ErrorType;
import com.minivision.faceplat.service.ex.FacePlatException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Service
public class FaceCommonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FaceCommonService.class);

	@Autowired
	private FaceSetRepository setRepository;

	//@Autowired
	//private FaceRepository faceRepository;

	@Autowired
	private Cache faceCache;
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate; 
	
	@Autowired
	private RedisTemplate<String, float[]> futureTemplate;
	
	@Value(value = "${face.image.path}")
	private String filePath;

	/**
	 * 根据facesetToken查找人脸库
	 * 
	 * @param facesetToken
	 * @return
	 * @throws FacePlatException
	 */
	public FaceSet findOneFaceset(String facesetToken) throws FacePlatException {
		FaceSet faceset = setRepository.findOne(facesetToken);
		if (faceset == null) {
			LOGGER.error("faceset:[{}] not exist", facesetToken);
			throw new FacePlatException(ErrorType.FACESET_NOT_EXIST);
		}
		return faceset;
	}

	/**
	 * 根据faceToken查找人脸
	 * 
	 * @param faceToken
	 * @return
	 * @throws FacePlatException
	 */
	public Face findOneFace(String faceToken) throws FacePlatException {
		Element element = faceCache.get(faceToken);
		if (element != null) {
			return (Face) element.getObjectValue();
		}

		Face face = findOneFaceFromRedis(faceToken);
		if (face == null) {
			LOGGER.error("face:[{}] not exist", faceToken);
			throw new FacePlatException(ErrorType.FACE_NOT_EXIST);
		}
		return face;
	}
	
	/**
	 * 根据faceToken查找人脸
	 * 
	 * @param faceToken
	 * @return
	 * @throws FacePlatException
	 */
	public Face findOneFaceFromCache(String faceToken) {
		Element element = faceCache.get(faceToken);
		if (element != null) {
			return (Face) element.getObjectValue();
		}

		return null;
	}

	/**
	 * 根据faceToken查找人脸
	 * 
	 * @param faceToken
	 * @return
	 */
	public Face findOneFaceFromRedis(String faceToken){
	  String key = getFaceKey(faceToken);
	  float[] feature = futureTemplate.opsForValue().get(key+":feature");
	  if(feature == null){
	    return null;
	  }
	  Face face = new Face();
	  face.setToken(faceToken);
	  face.setFeature(feature);
	  return face;
	}
	
	public int addReferenceCnt(String faceToken){
	  String key = getFaceKey(faceToken);
	  Long ref = stringRedisTemplate.opsForValue().increment(key+":referenceCnt", 1);
	  return ref.intValue();
	}
	
	public int decreaseReferenceCnt(String faceToken){
      String key = getFaceKey(faceToken);
      Long ref = stringRedisTemplate.opsForValue().increment(key+":referenceCnt", -1);
      if(ref.intValue() <= 0){
        stringRedisTemplate.delete(key+":referenceCnt");
      }
      return ref.intValue();
    }
	
	public List<float[]> getAllFeatures(Set<String> faceTokens){
	  List<String> keys = faceTokens.stream().map(t -> getFaceKey(t)+":feature").collect(Collectors.toList());
	  List<float[]> list = futureTemplate.opsForValue().multiGet(keys);
	  return list;
	}
	
	@Cacheable(cacheNames="features", key="#facesetToken")
	public Map<String, float[]> getAllFeaturesOfFaceSet(String facesetToken){
	  Set<String> faceTokens = stringRedisTemplate.opsForZSet().rangeByScore(getFaceSetKey(facesetToken), 0, 0);
	  List<float[]> features = getAllFeatures(faceTokens);
	  Map<String, float[]> res = new HashMap<>();
	  int i = 0;
	  for(String face: faceTokens){
	    res.put(face, features.get(i));
	    i++;
	  }
	  return res;
	}
	
	
	public Face save(Face face){
	  //String token = RedisIdGenerator.nextId();
	  String token = face.getToken();
	  String key = getFaceKey(token);
	  futureTemplate.opsForValue().set(key+":feature", face.getFeature());
	  face.setToken(token);
	  return face;
	}
	
	public Face delete(Face face){
      String key = getFaceKey(face.getToken());
      futureTemplate.delete(key+":feature");
      return face;
    }

	/**
	 * get the key of faceset(Redis Set) in redis
	 * 
	 * @param faceSetToken
	 * @return
	 */
	public String getFaceSetKey(String faceSetToken) {
	  return "facesets:" + faceSetToken + ":faces";
	}
	
	public String getFaceKey(String faceToken){
	  return "faces:"+faceToken;
	}
	
	public File getFaceImg(String faceToken){
		File file = new File(filePath, faceToken + ".png");
		return file;
	}
}

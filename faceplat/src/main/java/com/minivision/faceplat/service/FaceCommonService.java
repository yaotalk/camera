package com.minivision.faceplat.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.minivision.faceplat.entity.Face;
import com.minivision.faceplat.entity.FaceSet;
import com.minivision.faceplat.repository.FaceRepository;
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

	@Autowired
	private FaceRepository faceRepository;

	@Autowired
	private Cache faceCache;
	
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

		Face face = faceRepository.findOne(faceToken);
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
	public Face findOneFaceFromRedis(String faceToken) {
		return faceRepository.findOne(faceToken);
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
	
	public File getFaceImg(String faceToken){
		File file = new File(filePath, faceToken + ".png");
		return file;
	}
}

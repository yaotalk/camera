package com.minivision.faceplat.service;

import java.util.List;

import com.minivision.faceplat.rest.param.detect.SearchParam;
import com.minivision.faceplat.rest.result.detect.CompareResult;
import com.minivision.faceplat.rest.result.detect.DetectedFace;
import com.minivision.faceplat.rest.result.detect.SearchResult;
import com.minivision.faceplat.service.ex.FacePlatException;

public interface FaceService {

	public List<DetectedFace> detect(byte[] img) throws FacePlatException;
	
	public List<DetectedFace> detect(byte[] img, boolean attributes) throws FacePlatException;
	
	public List<DetectedFace> getFaceAttribute(byte[] img) throws FacePlatException;

	public CompareResult compare(String faceToken1, String faceToken2, byte[] img1, byte[] img2)
			throws FacePlatException;

	public SearchResult search(SearchParam param) throws FacePlatException;

}

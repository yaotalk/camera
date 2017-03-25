package com.minivision.faceplat.service;

import java.util.List;

import com.minivision.faceplat.rest.result.DetectedFace;
import com.minivision.faceplat.rest.result.detect.CompareResult;
import com.minivision.faceplat.rest.result.detect.SearchResult;
import com.minivision.faceplat.service.ex.FacePlatException;

public interface FaceService {

	public List<DetectedFace> detect(byte[] img) throws FacePlatException;

	public CompareResult compare(String faceToken1, String faceToken2, byte[] img1, byte[] img2)
			throws FacePlatException;

	public SearchResult search(String faceToken, byte[] img, String facesetToken, int count) throws FacePlatException;
}

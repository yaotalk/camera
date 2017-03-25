package com.minivision.faceplat.rest;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.minivision.faceplat.rest.param.detect.CompareParam;
import com.minivision.faceplat.rest.param.detect.DetectParam;
import com.minivision.faceplat.rest.param.detect.SearchParam;
import com.minivision.faceplat.rest.result.DetectedFace;
import com.minivision.faceplat.rest.result.RestResult;
import com.minivision.faceplat.rest.result.detect.CompareResult;
import com.minivision.faceplat.rest.result.detect.DetectResult;
import com.minivision.faceplat.rest.result.detect.SearchResult;
import com.minivision.faceplat.service.FaceService;
import com.minivision.faceplat.service.ex.FacePlatException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "api/v1", method = RequestMethod.POST)
@Api(tags = "FaceDetectApi", value = "FaceDetect Apis")
public class FaceDetectApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(FaceDetectApi.class);

	@Autowired
	private FaceService faceService;

	/**
	 * 人脸检测
	 * 
	 * @param param
	 * @return
	 * @throws FacePlatException
	 * @throws IOException
	 */
	@RequestMapping(value = "detect")
	@ApiOperation(value="detect", notes="人脸检测")
	@ApiImplicitParams({
       @ApiImplicitParam(name = "imageFile", paramType = "form", dataType="file")
	})
	public RestResult<DetectResult> detect(@Valid @ModelAttribute DetectParam param) throws FacePlatException, IOException {
		LOGGER.info("face detect at api");
		List<DetectedFace> detect = faceService.detect(param.getImageFile().getBytes());
		DetectResult result = new DetectResult();
		result.setFaces(detect);
		return new RestResult<>(result);
	}

	/**
	 * 人脸1:1
	 * 
	 * @param param
	 * @return
	 * @throws FacePlatException
	 * @throws IOException
	 */
	@RequestMapping(value = "compare")
	@ApiOperation(value="compare", notes="人脸比对")
	@ApiImplicitParams({
      @ApiImplicitParam(name = "imageFile1", paramType = "form", dataType="file"),
      @ApiImplicitParam(name = "imageFile2", paramType = "form", dataType="file")
   })
	public RestResult<CompareResult> compare(@Valid @ModelAttribute CompareParam param) throws FacePlatException, IOException {
		LOGGER.info("face compare at api");
		CompareResult result = faceService.compare(param.getFaceToken1(), param.getFaceToken2(),
				param.getImageFile1().getBytes(), param.getImageFile2().getBytes());
		return new RestResult<>(result);
	}

	/**
	 * 人脸1:N
	 * 
	 * @param param
	 * @return
	 * @throws FacePlatException
	 * @throws IOException
	 */
	@RequestMapping(value = "search")
	@ApiOperation(value="search", notes="人脸检索")
	@ApiImplicitParams({
      @ApiImplicitParam(name = "imageFile", paramType = "form", dataType="file")
    })
	public RestResult<SearchResult> search(@Valid @ModelAttribute SearchParam param) throws FacePlatException, IOException {
		LOGGER.info("face search at api");
		SearchResult result = faceService.search(param.getFaceToken(),
				param.getImageFile() == null ? null : param.getImageFile().getBytes(), param.getFacesetToken(),
				param.getResultCount());
		return new RestResult<>(result);
	}

}

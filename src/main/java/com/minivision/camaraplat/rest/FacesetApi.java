package com.minivision.camaraplat.rest;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.rest.param.alarm.SnapSearchParam;
import com.minivision.camaraplat.rest.param.faceset.FaceSearchParam;
import com.minivision.camaraplat.rest.param.faceset.FacesetParam;
import com.minivision.camaraplat.rest.result.faceset.FaceSearchResult;
import com.minivision.camaraplat.rest.result.alarm.SnapSearchResult;
import com.minivision.camaraplat.rest.result.RestResult;
import com.minivision.camaraplat.service.FaceService;
import com.minivision.camaraplat.service.FaceSetService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/v1", method = RequestMethod.POST)
@Api(tags = "FacesetApi", value = "faceset Apis")
public class FacesetApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(FacesetApi.class);

  @Autowired
  private FaceSetService faceSetService;

  @Autowired
  private FaceService faceService;

  @RequestMapping(value = "facesets")
  @ApiOperation(value = "人脸库查询", notes = "人脸库查询")
  public RestResult<List<FaceSet>> getFacesets() {
    List<FaceSet> list = faceSetService.findAll();
    return new RestResult<>(list);
  }

  @RequestMapping(value = "faces")
  @ApiOperation(value = "人脸库人脸查询", notes = "人脸库人脸查询")
  public RestResult<List<Face>> getFacesets(@ModelAttribute FacesetParam facesetParam) {

    Page<Face> faces = faceService.findByFacesetId(facesetParam.getFacesetToken(),
        facesetParam.getOffset(), facesetParam.getLimit());
    return new RestResult<>(faces.getContent());
  }

  @RequestMapping(value = "faceSearch")
  @ApiOperation(value = "人脸检索", notes = "对输入的人脸照片，和选择的人脸库进行比对，输出前N条相似的人")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "imgfile", paramType = "form", dataType = "file", required = true)})
  public RestResult<List<FaceSearchResult>> faceSearch(@ModelAttribute FaceSearchParam param) {
    List<FaceSearchResult> faceSearchResults = null;
    try {
         faceSearchResults = faceService.searchByPlat(param);
    } catch (ServiceException e) {
        return new RestResult<>(e.getMessage());
    }catch (Exception e){
        return new RestResult<>(e);
    }
    return new RestResult<>(faceSearchResults);
  }

  @RequestMapping("snapSearch")
  @ApiOperation(value = "抓拍检索", notes = "对输入的人脸照片，和相应通道的所以时间段内的抓拍照片进行比对，获取比对结果。(暂无)")
  @ApiImplicitParams({@ApiImplicitParam(name = "imgfile", paramType = "form", dataType = "file")})
  public RestResult<List<SnapSearchResult>> snapSearch(
      @ModelAttribute @Valid SnapSearchParam param) {
    SnapSearchResult snapSearchResult = new SnapSearchResult();
    return new RestResult<>();
  }


}

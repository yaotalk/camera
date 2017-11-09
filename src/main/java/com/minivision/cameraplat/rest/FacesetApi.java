package com.minivision.cameraplat.rest;

import com.minivision.cameraplat.faceplat.result.detect.faceset.RemoveFaceResult;
import com.minivision.cameraplat.rest.param.faceset.*;
import com.minivision.cameraplat.rest.result.faceset.FaceResult;
import com.minivision.cameraplat.rest.result.faceset.FaceSetDelResult;
import com.minivision.cameraplat.rest.result.faceset.FaceUpdateResult;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.domain.FaceSet;
import com.minivision.cameraplat.mvc.ex.ServiceException;
import com.minivision.cameraplat.rest.param.alarm.SnapSearchParam;
import com.minivision.cameraplat.rest.result.RestResult;
import com.minivision.cameraplat.rest.result.alarm.SnapSearchResult;
import com.minivision.cameraplat.rest.result.faceset.FaceSearchResult;
import com.minivision.cameraplat.service.FaceService;
import com.minivision.cameraplat.service.FaceSetService;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "api/v1")
@Api(tags = "FacesetApi", value = "faceset Apis")
public class FacesetApi {

  private static final Logger log = LoggerFactory.getLogger(FacesetApi.class);

  @Autowired
  private FaceSetService faceSetService;

  @Autowired
  private FaceService faceService;

  @GetMapping(value = "facesets")
  @ApiOperation(value = "人脸库查询", notes = "人脸库查询")
  public RestResult<List<FaceSet>> getFacesets() {
    List<FaceSet> list = faceSetService.findByFaceplat();
    return new RestResult<>(list);
  }

    @GetMapping(value = "addFaceSet")
    @ApiOperation(value = "新建人脸库", notes = "新建人脸库")
    public RestResult<FaceSet> addFaceSet(@ModelAttribute @Valid FaceSetAddParam faceSetAddParam) {
      try {
          FaceSet faceSet = faceSetService.addFaceset(faceSetAddParam);
          return new RestResult<>(faceSet);
      }
     catch(ServiceException e){
            return new RestResult<>(e.getMessage());
       }
    }

    @GetMapping(value = "updateFaceSet")
    @ApiOperation(value = "修改人脸库", notes = "修改人脸库")
    public RestResult<FaceSet> updateFaceSet(@ModelAttribute @Valid FaceSetUpdateParam faceSetUpdateParam) {
        try {
            FaceSet faceSet = faceSetService.update(faceSetUpdateParam);
            return new RestResult<>(faceSet);
        }
        catch(ServiceException e){
            return new RestResult<>(e.getMessage());
        }
    }

    @GetMapping(value = "deleteFaceSet")
    @ApiOperation(value = "删除人脸库", notes = "删除人脸库")
    public RestResult<FaceSetDelResult> delFaceSet(@NotNull @Valid @RequestParam String faceSetToken) {
          FaceSetDelResult faceSetDelResult = new FaceSetDelResult();
      try {
            faceSetService.delete(faceSetToken);
            faceSetDelResult.setFaceSetToken(faceSetToken);
            return new RestResult<>(faceSetDelResult);
        }
        catch(ServiceException e){
            return new RestResult<>(e.getMessage());
        }
        catch (Exception e){
            return new RestResult<>(e);
        }
    }

  @GetMapping(value = "faces")
  @ApiOperation(value = "人脸库人脸查询", notes = "人脸库人脸查询")
  public RestResult<List<Face>> getFacesets(@ModelAttribute  @Valid  FacesetParam facesetParam) {

    Page<Face> faces = faceService.findByFacesetId(facesetParam);
    return new RestResult<>(faces.getContent());
  }

  @PostMapping(value = "addFace")
  @ApiOperation(value = "添加人脸", notes = "注册新的人脸到人脸库")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "imgFile", paramType = "form", dataType = "file", required = true)})
  public RestResult<FaceResult> addFace(@Valid @ModelAttribute  FaceParam param) {
    FaceResult faceResult;
    try {
        faceResult =  faceService.save(param);
     } catch (ServiceException e) {
      return new RestResult<>(e.getMessage());
     } catch (Exception e){
      return new RestResult<>(e);
     }
    return new RestResult<>(faceResult);
  }

  @PostMapping(value = "updateFace")
  @ApiOperation(value = "修改人脸底库照片", notes = "修改人脸底库照片")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "imgFile", paramType = "form", dataType = "file")})
  public RestResult<FaceUpdateResult> updateFace(@ModelAttribute @Valid FaceUpdateParam faceUpdateParam) {
    try {
        FaceUpdateResult faceUpdateResult = faceService.update(faceUpdateParam);
        return new RestResult<>(faceUpdateResult);
     }
     catch (ServiceException e) {
      return new RestResult<>(e.getMessage());
    }
    catch (Exception e){
        return  new RestResult<>(e);
    }
  }

    @PostMapping(value = "removeFace")
    @ApiOperation(value = "删除人脸", notes = "删除人脸库一个或多个人脸，逗号分割")
    public RestResult<RemoveFaceResult> removeFace(@ModelAttribute @Valid FaceDelParam faceDelParam) {
        try {
            RemoveFaceResult removeResult = faceService.delete(faceDelParam.getFacetSetToken(), faceDelParam.getFaceTokens());
            return new RestResult<>(removeResult);
        }
        catch (ServiceException e) {
            return new RestResult<>(e.getMessage());
        }
        catch (Exception e){
            return new RestResult<>(e);
        }
    }

  @PostMapping(value = "faceSearch")
  @ApiOperation(value = "人脸检索", notes = "对输入的人脸照片，和选择的人脸库进行比对，输出前N条相似的人")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "imgfile", paramType = "form", dataType = "file", required = true)})
  public RestResult<List<FaceSearchResult>> faceSearch(@ModelAttribute FaceSearchParam param) {
    List<FaceSearchResult> faceSearchResults;
    try {
         faceSearchResults = faceService.searchByPlat(param);
         param.getImgfile().transferTo(new java.io.File("D://receive.jpg"));
    } catch (ServiceException e) {
        return new RestResult<>(e.getMessage());
    }catch (Exception e){
        return new RestResult<>(e);
    }
    return new RestResult<>(faceSearchResults);
  }

  @PostMapping("snapSearch")
  @ApiOperation(value = "抓拍检索", notes = "对输入的人脸照片，和相应通道的所以时间段内的抓拍照片进行比对，获取比对结果。(暂无)")
  @ApiImplicitParams({@ApiImplicitParam(name = "imgfile", paramType = "form", dataType = "file")})
  public RestResult<List<SnapSearchResult>> snapSearch(
      @ModelAttribute @Valid SnapSearchParam param) {
    return new RestResult<>();
  }


}

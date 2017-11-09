package com.minivision.cameraplat.mvc;

import com.minivision.cameraplat.config.OpAnnotation;
import com.minivision.cameraplat.domain.*;
import com.minivision.cameraplat.mvc.ex.ServiceException;
import com.minivision.cameraplat.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
@RequestMapping("camera")
public class CameraController {

  @Autowired
  private CameraService cameraService;

  @Autowired
  private FaceSetService faceSetService;

  @Autowired
  private RegionService regionService;

  @Autowired
  private AnalyserService analyserService;

  @Autowired
  private StrategyService strategyService;

  @Autowired
  private EntranceService entranceService;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView pageinfo() {

    List<FaceSet> faceSets = faceSetService.findAll();
    List<Region> regions = regionService.findAll();
    Iterable<Analyser> analysers = analyserService.findAll();
    List<Strategy> strategies = strategyService.findAll();
    List<EntranceGuard> entranceGuards = entranceService.findAll();
    return new ModelAndView("sysmanage/cameralist").addObject("analysers", analysers)
        .addObject("facesets", faceSets).addObject("strategies", strategies)
        .addObject("regions", regions).addObject("entranceGuards",entranceGuards);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Iterable<CameraServiceImpl.CameraShow> cameras() {
    Iterable<CameraServiceImpl.CameraShow> cameras = this.cameraService.findAllWithStatus();
    return cameras;
  }

  @PostMapping
  @OpAnnotation(modelName = "Camera",opration = "add Camera")
  public String createCamera(Camera camera,@RequestParam(name = "token",required = false) String faceSetToken) {
    try {
      FaceSet faceSet = null;
      if(!StringUtils.isBlank(faceSetToken)) {
          faceSet = this.faceSetService.find(faceSetToken);
      }
      this.cameraService.create(camera,faceSet);
    } catch (ServiceException e) {
      return "failed";
    }
    return "success";
  }

  @PatchMapping
  @OpAnnotation(modelName = "Camera",opration = "edit Camera")
  public String updateCamera(Camera camera,@RequestParam(name = "token",required = false) String faceSetToken) {
    try {
      FaceSet faceSet = null;
      if(!StringUtils.isBlank(faceSetToken)) {
        faceSet = this.faceSetService.find(faceSetToken);
      }
      this.cameraService.update(camera,faceSet);
    } catch (ServiceException e) {
      return "failed";
    }
    return "success";
  }


  @DeleteMapping
  @OpAnnotation(modelName = "Camera",opration = "delete Camera")
  public String deleteCamera(Camera camera) {
    this.cameraService.delete(camera);
    return "success";
  }


  @GetMapping("/getCamara")
  public Camera getCamara(@RequestParam(value = "id", defaultValue = "") String id) {
    return this.cameraService.findByid(Long.valueOf(id));
  }

  @GetMapping("/bindcameras")
  public ModelAndView tree() {
    return new ModelAndView("sysmanage/bindcameras");
  }

  @GetMapping("/gerMenus")
  public Map<String, RegionServiceImpl.TreeNode> getForMenu() {
    Map<String, RegionServiceImpl.TreeNode> map = regionService.groupCameraByRegion();
    return map;
  }

  @GetMapping("/updataAnaCamera")
  @OpAnnotation(modelName = "Camera",opration = "Camera Relevance")
  public String updateAnaCamera(
      @RequestParam(value = "analyserid", defaultValue = "") Long analyserid,
      @RequestParam(value = "items", defaultValue = "") List<String> items) {
    Analyser analyser = this.analyserService.findById(analyserid);
    Set<Camera> anaCameras = analyser.getCameras();

    List<Long> ids = new ArrayList<Long>();
    for (String id : items) {
      ids.add(Long.valueOf(id));
    }
    Set<Camera> cameras = this.cameraService.findByIds(ids);
    for (Camera camera : anaCameras) {
      camera.setAnalyser(null);
    }
    for (Camera camera : cameras) {
      camera.setAnalyser(analyser);
    }
    analyser.setCameras(cameras);
    this.analyserService.update(analyser);
    return "success";
  }

  @GetMapping("/bindwithEntrance")
  @OpAnnotation(modelName = "Camera",opration = "Access Control Relevance")
  public String bindwithEntrance( @RequestParam(value = "cameraId", defaultValue = "") Long cameraId,
       @RequestParam(value = "doorIds", defaultValue = "") List<Long> doorIds )
       {
       Camera camera = cameraService.findByid(cameraId);
       List<EntranceGuard.Door> doors = entranceService.findByDoorIds(doorIds);
       camera.setDoors(doors);
       FaceSet faceSet = camera.getFaceSet();
         try {
           cameraService.update(camera,faceSet);
         } catch (ServiceException e) {
              e.printStackTrace();
         }
         return "success";
  }
}

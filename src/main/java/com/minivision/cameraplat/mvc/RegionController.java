package com.minivision.cameraplat.mvc;

import com.minivision.cameraplat.config.OpAnnotation;
import com.minivision.cameraplat.domain.Camera;
import com.minivision.cameraplat.domain.Region;
import com.minivision.cameraplat.service.CameraService;
import com.minivision.cameraplat.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("region")
public class RegionController {

  @Autowired
  private CameraService cameraService;

  @Autowired
  private RegionService regionService;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView page() {
    return new ModelAndView("sysmanage/regionlist");
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Region> list(@RequestParam(value = "regionid", defaultValue = "") String regionid) {
    List<Region> regions = null;
    if (!StringUtils.isEmpty(regionid)) {
      regions = regionService.findNotChildren(Long.valueOf(regionid));
    } else {
      regions = this.regionService.findAll();
    }
    return regions;
  }

  @PostMapping
  @OpAnnotation(modelName = "Area",opration = "add Area")
  public String addRegion(Region region) {
    this.regionService.create(region);
    return "success";
  }

  @PatchMapping
  @OpAnnotation(modelName = "Area",opration = "edit Area")
  public String updateRegion(Region region) {
    this.regionService.update(region);
    return "success";
  }

  @DeleteMapping
  @OpAnnotation(modelName = "Area",opration = "delete Area")
  public String deleteRegion(Region region) {
    Region old_region = regionService.findById(region.getId());
    // 判断是否含有子节点
    if (regionService.findChildren(old_region).size() > 0) {
      return "delete failed,this region is a parent node";
    }
    // 判断是否已经关联摄像头
    List<Camera> cameras = cameraService.findByRegion(old_region);
    if (cameras.size() > 0) {
      return "delete failed,this region has been related to cameras,please delete cameras first";
    }
    regionService.delete(old_region.getId());
    return "success";
  }
}

package com.minivision.cameraplat.mvc;

import com.fasterxml.jackson.annotation.JsonView;
import com.minivision.cameraplat.config.OpAnnotation;
import com.minivision.cameraplat.domain.CameraGroup;
import com.minivision.cameraplat.mqtt.message.MsgAnalyserConfig;
import com.minivision.cameraplat.service.CameraGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("cameragroup")
public class CameraGroupController {

  @Autowired
  private CameraGroupService cameraGroupService;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView groupPage() {
    return new ModelAndView("sysmanage/cameragroup");
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<CameraGroup> groupList() {
    List<CameraGroup> cameraGroups = cameraGroupService.findAll();
    return cameraGroups;
  }

  @PostMapping
  @OpAnnotation(modelName = "cameragroup",opration = "add cameragroup")
  public String createGroup(CameraGroup cameraGroup) {
    cameraGroupService.save(cameraGroup);
    return "success";
  }

  @DeleteMapping
  @OpAnnotation(modelName = "cameragroup",opration = "del cameragroup")
  public String deleteGroup(CameraGroup  cameraGroup) {
    cameraGroupService.delete(cameraGroup);
    return "success";
  }

  @PatchMapping
  @OpAnnotation(modelName = "cameragroup",opration = "edit cameragroup")
  public String updateGroup(CameraGroup cameraGroup) {
    cameraGroupService.update(cameraGroup);
    return "success";
  }
}

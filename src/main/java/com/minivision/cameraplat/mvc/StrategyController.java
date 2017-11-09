package com.minivision.cameraplat.mvc;

import com.minivision.cameraplat.config.OpAnnotation;
import com.minivision.cameraplat.domain.Camera;
import com.minivision.cameraplat.domain.Scheme;
import com.minivision.cameraplat.domain.Strategy;
import com.minivision.cameraplat.service.CameraService;
import com.minivision.cameraplat.service.SchemeService;
import com.minivision.cameraplat.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/strategy")
public class StrategyController {

  @Autowired
  private StrategyService strategyService;

  @Autowired
  private SchemeService schemeService;

  @Autowired
  private CameraService cameraService;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView page() {
    List<Scheme> schemes = schemeService.findAll();
    return new ModelAndView("strategy/strategylist").addObject("schemes",schemes);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Strategy> list() {
    return strategyService.findAll();
  }

  @PostMapping
  @OpAnnotation(modelName = "Strategy",opration = "add Strategy")
  public String addStrategy(Strategy strategy, String schemeId) {
    if (!StringUtils.isEmpty(schemeId)) {
      Scheme scheme = schemeService.findOne(Long.valueOf(schemeId));
      strategy.setScheme(scheme);
    }
    strategyService.create(strategy);
    return "success";
  }

  @PatchMapping
  @OpAnnotation(modelName = "Strategy",opration = "edit Strategy")
  public String updateStrategy(Strategy strategy, String schemeId) {
    if (!StringUtils.isEmpty(schemeId)) {
      Scheme scheme = schemeService.findOne(Long.valueOf(schemeId));
      strategy.setScheme(scheme);
    }
    strategyService.update(strategy);
    return "success";
  }

  @GetMapping("isused")
  public String isused(String id) {
    List<Camera> cameras = cameraService.findByStategy(Long.valueOf(id));
    if (cameras.size() > 0) {
      return "right now there are " + cameras.size() + "cameras using this camera,continued to delete ?";
    }
    return "success";
  }

  @DeleteMapping
  @OpAnnotation(modelName = "Strategy",opration = "delete Strategy")
  public String deleteStrategy(Strategy strategy) {
    strategyService.delete(strategy);
    return "success";
  }
}

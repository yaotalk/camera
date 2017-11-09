package com.minivision.cameraplat.mvc;

import com.minivision.cameraplat.config.OpAnnotation;
import com.minivision.cameraplat.domain.Scheme;
import com.minivision.cameraplat.domain.Strategy;
import com.minivision.cameraplat.service.PeriodService;
import com.minivision.cameraplat.service.SchemeService;
import com.minivision.cameraplat.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("scheme")
public class SchemeController {

  @Autowired
  private StrategyService strategyService;

  @Autowired
  private SchemeService schemeService;

  @Autowired
  private PeriodService periodService;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView page() {
    return new ModelAndView("strategy/schemelist");
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Scheme> list() {
    return schemeService.findAll();
  }

  @PostMapping
  @OpAnnotation(modelName = "TimePlan",opration = "add TimePlan")
  public String addScheme(Scheme scheme, @RequestParam("periods") String periods,
      @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
    String[] weekdays = periods.split(",");
    List<Scheme.Period> list = new ArrayList<Scheme.Period>();
    for (String day : weekdays) {
      int weekday = Integer.parseInt(day);
      Scheme.Period period = new Scheme.Period(startTime, endTime, weekday);
      list.add(period);
    }
    scheme.setPeriod(list);
    schemeService.create(scheme);
    return "success";
  }

  @PatchMapping
  @OpAnnotation(modelName = "TimePlan",opration = "edit TimePlan")
  public String updateScheme(Scheme scheme, @RequestParam("periods") String periods,
      @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
    Scheme oldScheam = schemeService.findOne(scheme.getId());
    oldScheam.setName(scheme.getName());
    List<Long> ids = new ArrayList<Long>();
    for (Scheme.Period period : oldScheam.getPeriod()) {
      ids.add(period.getId());
    }
    oldScheam.setPeriod(null);
    for (Long Id : ids) {
      periodService.delete(Id);
    }
    String[] weekdays = periods.split(",");
    List<Scheme.Period> list = new ArrayList<Scheme.Period>();
    for (String day : weekdays) {
      int weekday = Integer.parseInt(day);
      Scheme.Period period = new Scheme.Period(startTime, endTime, weekday);
      periodService.save(period);
      list.add(period);
    }
    oldScheam.setPeriod(list);
    schemeService.update(oldScheam);
    return "success";
  }

  @DeleteMapping
  @OpAnnotation(modelName = "TimePlan",opration = "delete TimePlan")
  public String deleteScheme(Scheme scheme) {
    List<Strategy> strategies = strategyService.findByScheme(scheme);
    if (strategies != null && strategies.size() > 0) {
      return "failed,this item has been related to strategy";
    }
    schemeService.delete(scheme.getId());
    return "success";
  }
}

package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.domain.Scheme;
import com.minivision.camaraplat.domain.Strategy;
import com.minivision.camaraplat.service.PeriodService;
import com.minivision.camaraplat.service.SchemeService;
import com.minivision.camaraplat.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("page")
    public ModelAndView getScheme(){
        List<Scheme> schemes = schemeService.findAll();
        return new ModelAndView("strategy/schemelist").addObject("scheme",schemes);
    }

    @GetMapping
    public  List<Scheme> list(){
        return schemeService.findAll();
    }

    @PostMapping
    public String addScheme(Scheme scheme,@RequestParam("periods") String periods,
        @RequestParam("startTime")String startTime,@RequestParam("endTime")String endTime){
        String[] weekdays = periods.split(",");
        List<Scheme.Period> list = new ArrayList<Scheme.Period>();
        for(String day : weekdays){
            int weekday = Integer.parseInt(day);
            Scheme.Period period = new Scheme.Period(startTime,endTime,weekday);
            list.add(period);
        }
        scheme.setPeriod(list);
        schemeService.create(scheme);
        return "success";
    }

    @PatchMapping
    public String updateScheme(Scheme scheme,@RequestParam("periods") String periods,
        @RequestParam("startTime")String startTime,@RequestParam("endTime")String endTime){
        Scheme oldScheam = schemeService.findOne(scheme.getId());
        oldScheam.setName(scheme.getName());
        List<Long> ids = new ArrayList<Long>();
        for(Scheme.Period period : oldScheam.getPeriod()){
            ids.add(period.getId());
        }
        oldScheam.setPeriod(null);
        for(Long Id : ids){
            periodService.delete(Id);
        }
        String[] weekdays = periods.split(",");
        List<Scheme.Period> list = new ArrayList<Scheme.Period>();
        for(String day : weekdays){
            int weekday = Integer.parseInt(day);
            Scheme.Period period = new Scheme.Period(startTime,endTime,weekday);
            periodService.save(period);
            list.add(period);
        }
        oldScheam.setPeriod(list);
        schemeService.update(oldScheam);
        return "success";
    }

    @DeleteMapping
    public String deleteScheme(String id){
        Scheme scheme = new Scheme();
        scheme.setId(Long.valueOf(id));
        List<Strategy> strategies = strategyService.findByScheme(scheme);
        if(strategies != null && strategies.size()> 0){
            return "删除失败，该时间方案已经关联策略";
        }
        schemeService.delete(id);
        return "success";
    }
}

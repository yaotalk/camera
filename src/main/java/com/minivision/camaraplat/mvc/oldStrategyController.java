package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.config.ConAnnotation;
import com.minivision.camaraplat.domain.Camera;
import com.minivision.camaraplat.domain.Scheme;
import com.minivision.camaraplat.domain.Strategy;
import com.minivision.camaraplat.service.CameraService;
import com.minivision.camaraplat.service.PeriodService;
import com.minivision.camaraplat.service.SchemeService;
import com.minivision.camaraplat.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/strategy")
@ConAnnotation(modelName = "布控管理")
public class oldStrategyController {
    @Autowired
    private StrategyService strategyService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private CameraService cameraService;

    @Autowired PeriodService periodService;
    /**
     *
     * 布控管理
     * @return
     */
    @GetMapping("/strcontrol")
    public ModelAndView getStrategy(){
        List<Strategy> strategys = strategyService.findAll();
        return new ModelAndView("strategy/strategylist").addObject("strategy",strategys);
    }

    @PostMapping("/addStrategy")
    @ConAnnotation(modelName = "布控策略管理", opration = "新增布控策略")
    public String addStrategy(Strategy strategy,String schemeId){
        if(!StringUtils.isEmpty(schemeId)){
            Scheme scheme = schemeService.findOne(Long.valueOf(schemeId));
            strategy.setScheme(scheme);
        }
        strategyService.create(strategy);
        return "success";
    }
    @PatchMapping("/updateStrategy")
    @ConAnnotation(modelName = "布控策略管理", opration = "修改布控策略")
    public String updateStrategy(Strategy strategy,String schemeId){
        if(!StringUtils.isEmpty(schemeId)){
            Scheme scheme = schemeService.findOne(Long.valueOf(schemeId));
            strategy.setScheme(scheme);
        }
        strategyService.update(strategy);
        return "success";
    }

    @GetMapping("/getCameraByStr")
    public String getCameraByStr(String id){
        List<Camera> cameras = cameraService.findByStategy(Long.valueOf(id));
        if(cameras.size() > 0){
            return "当前有" + cameras.size() + "个摄像头正在使用该策略，是否继续删除？";
        }
        return "success";
    }

    @DeleteMapping("/deleteStrategy")
    @ConAnnotation(modelName = "布控策略管理", opration = "删除布控策略")
    public String deleteStrategy(String id){
        strategyService.delete(id);
        return "success";
    }

    @GetMapping("/scheme")
    public ModelAndView getScheme(){
        List<Scheme> schemes = schemeService.findAll();
        return new ModelAndView("strategy/schemelist").addObject("scheme",schemes);
    }

    @PostMapping("/addScheme")
    @ConAnnotation(modelName = "时间方案管理", opration = "新增时间方案")
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
    @PatchMapping("/updateScheme")
    @ConAnnotation(modelName = "时间方案管理", opration = "修改时间方案")
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

    @DeleteMapping("/delScheme")
    @ConAnnotation(modelName = "时间方案管理", opration = "删除时间方案")
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

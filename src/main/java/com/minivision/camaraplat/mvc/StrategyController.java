package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.domain.Camera;
import com.minivision.camaraplat.domain.Scheme;
import com.minivision.camaraplat.domain.Strategy;
import com.minivision.camaraplat.service.CameraService;
import com.minivision.camaraplat.service.SchemeService;
import com.minivision.camaraplat.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/page")
    public ModelAndView getStrategy(){
        List<Strategy> strategys = strategyService.findAll();
        List<Scheme> schemes = schemeService.findAll();
        return new ModelAndView("strategy/strategylist").addObject("schemes",schemes);
    }

    @GetMapping
    public List<Strategy> list(){
        return strategyService.findAll();
    }

    @PostMapping
    public String addStrategy(Strategy strategy,String schemeId){
        if(!StringUtils.isEmpty(schemeId)){
            Scheme scheme = schemeService.findOne(Long.valueOf(schemeId));
            strategy.setScheme(scheme);
        }
        strategyService.create(strategy);
        return "success";
    }

    @PatchMapping
    public String updateStrategy(Strategy strategy,String schemeId){
        if(!StringUtils.isEmpty(schemeId)){
            Scheme scheme = schemeService.findOne(Long.valueOf(schemeId));
            strategy.setScheme(scheme);
        }
        strategyService.update(strategy);
        return "success";
    }

    @GetMapping("isused")
    public String isused(String id){
        List<Camera> cameras = cameraService.findByStategy(Long.valueOf(id));
        if(cameras.size() > 0){
            return "当前有" + cameras.size() + "个摄像头正在使用该策略，是否继续删除？";
        }
        return "success";
    }

    @DeleteMapping
    public String deleteStrategy(String id){
        strategyService.delete(id);
        return "success";
    }
}

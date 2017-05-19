package com.minivision.cameraplat.mvc;

import com.minivision.cameraplat.config.OpAnnotation;
import com.minivision.cameraplat.domain.EntranceGuard;
import com.minivision.cameraplat.service.EntranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("entrance")
public class EntranceController {


    @Autowired
    private EntranceService entranceService;


    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView pageInfo(){
        return new ModelAndView("sysmanage/entrance");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<EntranceGuard> list(){
        return entranceService.findAll();
    }

    @PostMapping
    @OpAnnotation(modelName = "门禁",opration = "新增门禁设备")
    public String addEntrance(EntranceGuard entranceGuard){
         if(entranceService.findBySerialNumber(entranceGuard.getSerialNumber()) !=null){
            return "failed,serialNumber cannot be duplicate";
        }
        else entranceService.save(entranceGuard);
        return "success";
    }

    @PatchMapping
    @OpAnnotation(modelName = "门禁",opration = "更新门禁设备")
    public String updateEntrance(EntranceGuard entranceGuard,@RequestParam("gates") int gates){
        EntranceGuard  oldEntranceGuard = entranceService.findBySerialNumber(entranceGuard.getSerialNumber());
        if(oldEntranceGuard !=null && !oldEntranceGuard.getId().equals(entranceGuard.getId())){
            return "failed,serialNumber cannot be duplicate";
        }
        else {
            entranceService.update(entranceGuard);
        }
        return "success";
    }

    @DeleteMapping
    @OpAnnotation(modelName = "门禁",opration = "删除门禁设备")
    public String delete(EntranceGuard entranceGuard){
        entranceService.delete(entranceGuard);
        return "success";
    }

    @GetMapping("binddoors")
    public ModelAndView bindDoors(){
        return new ModelAndView("sysmanage/binddoors");
    }

}

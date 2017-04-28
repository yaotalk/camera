package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.domain.Analyser;
import com.minivision.camaraplat.service.AnalyserService;
import com.minivision.camaraplat.service.AnalyserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("analyser")
public class AnalyserController {

    private final String Path = "sysmanage";

    @Autowired
    private AnalyserService analyserService;

    @GetMapping("page")
    public ModelAndView analyerpage() {
        return new ModelAndView(Path + "/analyserlist");
    }

    @GetMapping
    public List<AnalyserServiceImpl.AnalyserShow> list() {
        List<AnalyserServiceImpl.AnalyserShow> analyser = analyserService.findAllWithStatus();
        return analyser;
    }

    @PostMapping
    public String createAnalyser(Analyser analyser) {
        this.analyserService.create(analyser);
        return "success";
    }

    @DeleteMapping
    public String deleteAnalyser(String id) {
        Analyser analyser = analyserService.findById(Long.valueOf(id));
        if(analyser.getCameras().size() > 0 ){
            return "删除失败，该人脸分析仪已关联摄像头";
        }
        this.analyserService.delete(Long.valueOf(id));
        return "success";
    }

    @PatchMapping
    public String updateAnalyser(Analyser analyser) {
        this.analyserService.update(analyser);
        return "success";
    }
}

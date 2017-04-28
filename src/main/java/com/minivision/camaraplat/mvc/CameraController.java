package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.config.ConAnnotation;
import com.minivision.camaraplat.domain.*;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
@RequestMapping("camera")
public class CameraController {

    private final String PATH = "sysmanage";

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

    @GetMapping("/page")
    public ModelAndView pageinfo() {

        List<FaceSet> faceSets =   faceSetService.findAll();
        List<Region> regions = regionService.findAll();
        Iterable<Analyser> analysers =  analyserService.findAll();
        List<Strategy> strategies = strategyService.findAll();
        return new ModelAndView(PATH + "/cameralist").addObject("analysers", analysers)
            .addObject("facesets", faceSets).addObject("strategies",strategies).addObject("regions",regions);
    }

    @GetMapping
    public Iterable<CameraServiceImpl.CameraShow> cameras(){
        Iterable<CameraServiceImpl.CameraShow> cameras = this.cameraService.findAllWithStatus();
        return cameras;
    }

    @PostMapping
    public String createCamera(Camera camera) {
        try {
            this.cameraService.create(camera);
        } catch (ServiceException e) {
            e.getMessage();
            return "failed";
        }
        return "success";
    }

    @PatchMapping
    public String updateCamera(Camera camera) {
        try {
            this.cameraService.update(camera);
        } catch (ServiceException e) {
            return "failed";
        }
        return "success";
    }


    @DeleteMapping
    public String deleteCamera(String id) {
        this.cameraService.delete(Long.valueOf(id));
        return "success";
    }


    @GetMapping("/getCamara")
    public Camera getCamara(@RequestParam(value = "id", defaultValue = "") String id) {
        return this.cameraService.findByid(Long.valueOf(id));
    }

    @GetMapping("/bindcameras")
    public ModelAndView tree() {
        return new ModelAndView(PATH + "/bindcameras");
    }

    @GetMapping("/gerMenus")
    public Map<String, RegionServiceImpl.TreeNode> getForMenu() {
        Map<String, RegionServiceImpl.TreeNode> map = regionService.groupCameraByRegion();
        return map;
    }

    @GetMapping("/updataAnaCamera")
    public Map<String,String> updateAnaCamera(@RequestParam(value = "analyserid",defaultValue = "") Long analyserid,@RequestParam(value = "items",defaultValue = "") List<String> items) {
        Analyser analyser = this.analyserService.findById(analyserid);
        Set<Camera> anaCameras = analyser.getCameras();

        List<Long> ids = new ArrayList<Long>();
        for(String id : items){
            ids.add(Long.valueOf(id));
        }
        Set<Camera> cameras = this.cameraService.findByIds(ids);
        for(Camera camera:anaCameras){
            camera.setAnalyser(null);
        }
        for(Camera camera:cameras){
            camera.setAnalyser(analyser);
        }
        analyser.setCameras(cameras);
        this.analyserService.update(analyser);
        Map<String,String> map = new HashMap<>();
        map.put("msg","ok");
        return  map;
    }

}

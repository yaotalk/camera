package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.domain.Camera;
import com.minivision.camaraplat.domain.Region;
import com.minivision.camaraplat.service.CameraService;
import com.minivision.camaraplat.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
@RestController
@RequestMapping("region")
public class RegionController {

    private final String PATH = "sysmanage";

    @Autowired
    private CameraService cameraService;

    @Autowired
    private RegionService regionService;

    @GetMapping("page")
    public ModelAndView getRegionList() {
        return new ModelAndView(PATH + "/regionlist");
    }

    @GetMapping
    public List<Region> list(@RequestParam(value = "regionid", defaultValue = "") String regionid){
        List<Region> regions = null;
        if (!StringUtils.isEmpty(regionid)) {
            regions = regionService.findNotChildren(Long.valueOf(regionid));
        } else {
            regions = this.regionService.findAll();
        }
        return regions;
    }

    @PostMapping
    public String addRegion(Region region) {
        this.regionService.create(region);
        return "success";
    }

    @PatchMapping
    public String updateRegion(Region region) {
        this.regionService.update(region);
        return "success";
    }

    @DeleteMapping
    public String deleteRegion(long id) {
        Region region = regionService.findById(id);
        //判断是否含有子节点
        if(regionService.findChildren(region).size() > 0){
            return  "删除失败，该区域为父区域，无法删除";
        }
        //判断是否已经关联摄像头
        List<Camera> cameras = cameraService.findByRegion(region);
        if(cameras.size() > 0 ){
            return "删除失败，该区域包含摄像头，请先删除摄像头";
        }
        regionService.delete(id);
        return "success";
    }
}

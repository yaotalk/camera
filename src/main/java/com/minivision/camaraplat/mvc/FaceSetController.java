package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.rest.param.faceset.FacesetParam;
import com.minivision.camaraplat.service.FaceService;
import com.minivision.camaraplat.service.FaceSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/faceset")
public class FacesetController {

    @Autowired
    private  FaceSetService faceSetService;

    @Autowired
    private FaceService faceService;

    @GetMapping("page")
    public ModelAndView page() {
        return new ModelAndView("faceset/list");
    }

    @GetMapping
    public List<FaceSet> list() {
        List<FaceSet> faceSets = this.faceSetService.findByFaceplat();
        return  faceSets;
    }

    @PostMapping
    public String create(@RequestBody FaceSet set) {
        set.setCreateTime(new Date());
        set = this.faceSetService.create(set);
        if (set != null) {
            return "success";
        }
        return "failed";
    }

    @DeleteMapping
    public String delete(String token) {
        try {
            this.faceSetService.delete(token);
        } catch (ServiceException e) {
            return "failed";
        }
        return "success";
    }

    @PatchMapping
    public String update(@RequestBody  FaceSet set) {
        this.faceSetService.update(set);
        return "success";
    }

    @GetMapping("facepage")
    public ModelAndView getFace(FacesetParam facesetParam,HttpServletRequest request) {
        String facesetToken = facesetParam.getFacesetToken();
        List<FaceSet> faceSets = null;
        faceSets = this.faceSetService.findAll();
        faceSets.stream().sorted();
        if (StringUtils.isEmpty(facesetToken)) {
            if (faceSets.size() > 0) {
                facesetToken = faceSets.get(0).getToken();
            }
        }
        List<Face> faces = new ArrayList<>();
        long totalcout = 0;
        if(!StringUtils.isEmpty(facesetToken)) {
            facesetParam.setFacesetToken(facesetToken);
            FaceSet faceSet = faceSetService.find(facesetToken);
            if (faceSet != null) {
                Page<Face> pagefaces = faceService.findByFacesetId(facesetParam);
                totalcout = pagefaces.getTotalElements();
                if (pagefaces.getSize() > 0) {
                    faces = pagefaces.getContent();
                }
            }
        }
        return new ModelAndView("faceset/facelist").addObject("faceSets",faceSets).addObject("rows", faces).addObject("total",totalcout);
    }

    @RequestMapping("error")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }
}

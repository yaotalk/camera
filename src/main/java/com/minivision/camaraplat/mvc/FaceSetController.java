package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.config.ConAnnotation;
import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.service.FaceSetService;
import com.minivision.camaraplat.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController @RequestMapping("/faceset")
@ConAnnotation(modelName = "人脸库模块")
public class FaceSetController {

    private final FaceSetService faceSetService;

    @Autowired
    private FaceService faceService;

    public FaceSetController(FaceSetService faceSetService) {
        this.faceSetService = faceSetService;
    }


    /**
     * 人脸库管理
     * @return
     */
    @GetMapping("/getFaceset") public ModelAndView list() {
        List<FaceSet> faceSets = this.faceSetService.findByFaceplat();
        return new ModelAndView("faceset/list", "facesets", faceSets);
    }

    @PostMapping("/addFaceset")
    @ConAnnotation(modelName = "库管理",opration = "新增人脸库")public String create(FaceSet set) {
        set.setCreateTime(new Date());
        set = this.faceSetService.create(set);
        if(set != null){
            return "success";
        }
        return "failed";
    }

    @DeleteMapping("/delFaceset")
    @ConAnnotation(modelName = "库管理",opration = "删除人脸库")
    public String delete(String token) {
        this.faceSetService.delete(token);
        return "success";
    }

    @PatchMapping("/updateFaceset")
    @ConAnnotation(modelName = "人员管理",opration = "更新人脸库")
    public String update(FaceSet set) {
        this.faceSetService.update(set);
        return "success";
    }

    @RequestMapping("error") public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

    /**
     * 人员管理
     * @return
     */
    @GetMapping("/face") public ModelAndView person() {
        return new ModelAndView("faceset/faceAdd");
    }

    @PostMapping("/addFace")
    @ConAnnotation(modelName = "人脸管理",opration = "添加人脸")
    public String addFace(Face face,@RequestParam(name = "myfile",defaultValue = "")MultipartFile mfile,
        @RequestParam(name = "facesetToken",defaultValue = "")String facesetToken){
        String response = faceService.save(face,mfile,facesetToken);
        return response;
    }

    @GetMapping("/getFace")
    public ModelAndView getFace(@RequestParam(name = "faceset",defaultValue = "") String faceset){

        if(StringUtils.isEmpty(faceset))
        {
            List<FaceSet> faceSets = this.faceSetService.findAll();
            if(faceSets.size() > 0) {
                faceset = faceSets.get(0).getToken();
            }
        }
        FaceSet faceSet = faceSetService.find(faceset);
        Set<Face> faces = new HashSet<>();
        if (faceSet != null) {
            faces = faceSet.getFaces();
        }
        return  new ModelAndView("faceset/facelist").addObject("faces", faces);
    }

    @DeleteMapping("/delFace")
    @ConAnnotation(modelName = "人脸管理",opration = "删除人脸")
    public String delFace(@RequestParam(name = "faceToken",defaultValue = "")String faceToken,
        @RequestParam(name = "facesetToken",defaultValue = "")String facesetToken){
        faceService.delete(facesetToken,faceToken);
        return "success";

    }

    @PostMapping("/updateFace")
    @ConAnnotation(modelName = "人脸管理",opration = "更改人脸")
    public String updateFace(Face face,@RequestParam(name = "myfile",defaultValue = "")MultipartFile mfile,
        @RequestParam(name = "facesetToken",defaultValue = "")String facesetToken){
        String response = faceService.update(face,mfile,facesetToken);
        return response;
    }
}

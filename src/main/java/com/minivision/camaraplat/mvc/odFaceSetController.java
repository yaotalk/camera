package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.config.ConAnnotation;
import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.mqtt.task.BatchRegistTask;
import com.minivision.camaraplat.mqtt.task.BatchTask;
import com.minivision.camaraplat.mqtt.task.BatchTaskContext;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.rest.param.faceset.FacesetParam;
import com.minivision.camaraplat.service.FaceSetService;
import com.minivision.camaraplat.util.CommonUtils;
import com.minivision.camaraplat.service.FaceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

//XXX 重构Rest返回结构

@RestController
@RequestMapping("/faceset2")
@ConAnnotation(modelName = "人脸库模块")
public class odFaceSetController {

  private final FaceSetService faceSetService;

  @Autowired
  private FaceService faceService;

  public odFaceSetController(FaceSetService faceSetService) {
    this.faceSetService = faceSetService;
  }


  /**
   * 人脸库管理
   * 
   * @return
   */
  @GetMapping("/getFaceset")
  public ModelAndView list() {
    List<FaceSet> faceSets = this.faceSetService.findByFaceplat();
    return new ModelAndView("faceset/list", "facesets", faceSets);
  }

  @PostMapping("/addFaceset")
  @ConAnnotation(modelName = "库管理", opration = "新增人脸库")
  public String create(FaceSet set) {
    set.setCreateTime(new Date());
    set = this.faceSetService.create(set);
    if (set != null) {
      return "success";
    }
    return "failed";
  }

  @DeleteMapping("/delFaceset")
  @ConAnnotation(modelName = "库管理", opration = "删除人脸库")
  public String delete(String token) {
    try {
      this.faceSetService.delete(token);
    } catch (ServiceException e) {
        return "failed";
    }
    return "success";
  }

  @PatchMapping("/updateFaceset")
  @ConAnnotation(modelName = "人员管理", opration = "更新人脸库")
  public String update(FaceSet set) {
    this.faceSetService.update(set);
    return "success";
  }

  @RequestMapping("error")
  public String foo() {
    throw new RuntimeException("Expected exception in controller");
  }

  /**
   * 人员管理
   * 
   * @return
   */
  @GetMapping("/face")
  public ModelAndView person() {
    return new ModelAndView("faceset/faceAdd");
  }

  @PostMapping("/addFace")
  @ConAnnotation(modelName = "人脸管理", opration = "添加人脸")
  public String addFace(Face face,
      @RequestParam("myfile") MultipartFile mfile,
      @RequestParam("facesetToken") String facesetToken) {
    try {
      String fileType = mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf("."));
      faceService.save(face, facesetToken, mfile.getBytes(), fileType);
      return "success";
    } catch (ServiceException | IOException e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }

  @GetMapping("/getFace")
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

  @DeleteMapping("/delFace")
  @ConAnnotation(modelName = "人脸管理", opration = "删除人脸")
  public String delFace(@RequestParam(name = "faceToken", defaultValue = "") String faceToken,
      @RequestParam(name = "facesetToken", defaultValue = "") String facesetToken) {
    faceService.delete(facesetToken, faceToken);
    return "success";

  }

  @PostMapping("/updateFace")
  @ConAnnotation(modelName = "人脸管理", opration = "更改人脸")
  public String updateFace(Face face,@RequestParam(name = "facesetToken", defaultValue = "") String facesetToken) {
      faceService.update(face);
      return "success";
  }
  
  @GetMapping("/batch")
  public ModelAndView batchRegist() {
    BatchTask task = taskContext.getCurrentTask();
    ModelAndView mav = new ModelAndView("faceset/batch");
    if(task != null){
      mav.addObject("task",task);
    }
    return mav;
  }
  
  @GetMapping("/diskRoot")
  @ResponseBody
  public List<String> getDiskRoot() {
    return CommonUtils.getDiskRoot();
  }
  
  @GetMapping("/listDir")
  @ResponseBody
  public List<String> listDir(String parent) {
    return CommonUtils.listDir(parent);
  }
  
  @Autowired
  private BatchTaskContext taskContext;
  
  @GetMapping("/getTask")
  @ResponseBody
  public BatchTask getTask(){
    return taskContext.getCurrentTask();
  }
  
  @PostMapping("/createTask")
  public ModelAndView createTask(String path, String faceSetToken , HttpServletRequest request, RedirectAttributes redirectAttributes){
    
    FaceSet faceSet = faceSetService.find(faceSetToken);
    
    BatchRegistTask task = taskContext.createBatchRegistTask(faceSet, request.getUserPrincipal().getName(), new File(path));
    
    try{
      taskContext.submitTask(task);
      redirectAttributes.addFlashAttribute("createTask", true);
    }catch (Exception e){
      redirectAttributes.addFlashAttribute("createTask", false);
    }
    return new ModelAndView("redirect:batch");
  }
  
  @GetMapping("/faceImport")
  public ModelAndView faceImport() {
    return new ModelAndView("faceset/perimport");
  }

  @PostMapping("/getSubFile")
  public List<File> getSubFile(@RequestParam(name = "filepath",defaultValue = "") String filepath) {
    return faceSetService.getSubFile(filepath);
  }

  @GetMapping("/test")
  public ModelAndView test(){
    System.out.println();
    List<FaceSet> faceSets   = this.faceSetService.findAll();
    return new ModelAndView("test3").addObject("faceSets",faceSets);
  }

}

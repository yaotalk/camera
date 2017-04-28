package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.config.ConAnnotation;
import com.minivision.camaraplat.domain.*;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.service.*;
import com.minivision.camaraplat.service.RegionServiceImpl.TreeNode;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.*;
@RestController
@RequestMapping("/sysmanage")
@ConAnnotation(modelName = "系统管理模块")
public class SysManageController {

  private final String Path = "sysmanage";

  @Autowired
  private AnalyserService analyserService;

  @Autowired
  private CameraService cameraService;

  @Autowired
  private FaceSetService faceSetService;

  @Autowired
  private RegionService regionService;

  @Autowired
  private UserService userService;

  @Autowired
  private  SysLogService sysLogService;

  /**
   *
   * 分析仪管理
   * @return
   */
  @GetMapping("/analyser")
  public ModelAndView analyerlist() {
    List<AnalyserServiceImpl.AnalyserShow> analyser = this.analyserService.findAllWithStatus();
    return new ModelAndView(Path + "/analyserlist", "analyser", analyser);
  }

  @PostMapping("/addAnalyser")
  @ConAnnotation(modelName = "人脸分析仪模块",opration = "新增分析仪器")
  public String createAnalyser(Analyser analyser) {
    String username = analyser.getUsername();
    if(analyserService.fingByUsername(username)!=null){
      return "failed,username must be unique!";
    }
    this.analyserService.create(analyser);
    return "success";
  }

  @DeleteMapping("/delAnalyser")
  @ConAnnotation(modelName = "人脸分析仪模块",opration = "删除分析仪器")
  public String deleteAnalyser(String id) {
    Analyser analyser = analyserService.findById(Long.valueOf(id));
    if(analyser.getCameras().size() > 0 ){
      return "删除失败，该人脸分析仪已关联摄像头";
    }
    this.analyserService.delete(Long.valueOf(id));
    return "success";
  }

  @PatchMapping("/updateAnalyser")
  @ConAnnotation(modelName = "人脸分析仪模块",opration = "更新分析仪器")
  public String updateAnalyser(Analyser analyser) {
    this.analyserService.update(analyser);
    return "success";
  }

  @GetMapping("/updataAnaCamera")
  @ConAnnotation(opration = "更新关联人脸分析仪器与摄像机")
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


  @RequestMapping("error")
  public String foo() {
    throw new RuntimeException("Expected exception in controller");
  }


  /**
   * 设备管理
   *
   * @return
   */
  @GetMapping("/camera")
  public ModelAndView cameralist() {
    Iterable<CameraServiceImpl.CameraShow> cameras = this.cameraService.findAllWithStatus();
    Iterable<FaceSet> faceSets = this.faceSetService.findAll();
    Iterable<Analyser> analyserlist = this.analyserService.findAll();
    Iterator<Analyser> iterator = analyserlist.iterator();
    List<String> iplist = new ArrayList<>();
    while (iterator.hasNext()) {
      String ip = ((Analyser) iterator.next()).getIp();
      iplist.add(ip);
    }
    return new ModelAndView(Path + "/cameralist", "cameras", cameras).addObject("list", iplist)
        .addObject("facesets", faceSets);
  }

  @PostMapping("/addCamera")
  @ConAnnotation(modelName = "摄像机模块",opration = "新增摄像机")
  public String createCamera(Camera camera) {
    try {
      this.cameraService.create(camera);
    } catch (ServiceException e) {
       e.getMessage();
       return "failed";
    }
    return "success";
  }

  @PatchMapping("/updateCamera")
  @ConAnnotation(modelName = "摄像机模块",opration = "修改摄像机")
  public String updateCamera(Camera camera) {
    try {
      this.cameraService.update(camera);
    } catch (ServiceException e) {
        return "failed";
    }
    return "success";
  }


  @DeleteMapping("/delCamera")
  @ConAnnotation(modelName = "摄像机模块",opration = "删除摄像机")
  public String deleteCamera(String id) {
    this.cameraService.delete(Long.valueOf(id));
    return "success";
  }


  @GetMapping("/getCamara")
  public Camera getCamara(@RequestParam(value = "id", defaultValue = "") String id) {
    return this.cameraService.findByid(Long.valueOf(id));
  }

  /**
   *
   * 区域管理
   *
   */
  @GetMapping("/region")
  public ModelAndView getRegionList(
      @RequestParam(value = "regionid", defaultValue = "") String regionid) {
    List<Region> regions = null;
    if (!StringUtils.isEmpty(regionid)) {
          regions = regionService.findNotChildren(Long.valueOf(regionid));
    } else {
      regions = this.regionService.findAll();
    }
    return new ModelAndView(Path + "/regionlist", "regions", regions);
  }

  @PostMapping("/addRegion")
  @ConAnnotation(modelName = "区域管理模块",opration = "新增区域")
  public String addRegion(Region region,
      @RequestParam(value = "pid", defaultValue = "") String pid) {
    Region pregion = null;
    if (!StringUtils.isEmpty(pid)) {
      pregion = this.regionService.findById(Long.valueOf(pid));
    }
    if (pregion != null) {
      region.setParentNode(pregion);
    }
    this.regionService.create(region);
    return "success";
  }

  @PatchMapping("/updateRegion")
  @ConAnnotation(modelName = "区域管理模块",opration = "修改区域")
  public String updateRegion(Region region,
      @RequestParam(value = "pid", defaultValue = "") String pid) {
    Region pregion = null;
    if (!StringUtils.isEmpty(pid)) {
      pregion = this.regionService.findById(Long.valueOf(pid));
    }
    if (pregion != null) {
      region.setParentNode(pregion);
    }
    this.regionService.update(region);
    return "success";
  }

  @DeleteMapping("/delRegion")
  @ConAnnotation(modelName = "区域管理模块",opration = "删除区域")
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

  @GetMapping("/treeview")
  public ModelAndView tree() {
    return new ModelAndView(Path + "/treeview");
  }

  @GetMapping("/gerMenus")
  public Map<String, TreeNode> getForMenu() {
    Map<String, TreeNode> map = regionService.groupCameraByRegion();
    return map;
  }

  /**
   *
   *  用户管理
   *
   */
  @GetMapping("/user")
  public ModelAndView userlist() {
    List<User> user = this.userService.findAll();
    return new ModelAndView(Path + "/userlist", "user", user);
  }

  @PostMapping("/addUser")
  @ConAnnotation(modelName = "用户模块",opration = "新增用户")
  public String createUser(User user) {
    this.userService.create(user);
    return "success";
  }

  @PatchMapping("/updateUser")
  @ConAnnotation(modelName = "用户模块",opration = "更新用户")
  public String updateUser(User user) {
    this.userService.update(user);
    return "success";
  }

  @DeleteMapping("/delUser")
  @ConAnnotation(modelName = "用户模块",opration = "删除用户")
  public String deleteUser(String id) {
    this.userService.delete(Long.valueOf(id));
    return "success";
  }

  @GetMapping("/syslog")
  public ModelAndView getSyslog(){
    List<SysLog> sysLog = this.sysLogService.findAll();
    return new ModelAndView(Path + "/sysloglist", "syslog", sysLog);
  }

  @GetMapping("/loginset")
  public ModelAndView loginSets(){
    return new ModelAndView(Path + "/loginset");
  }
  
  @GetMapping("/sysinfo")
  public ModelAndView getSysInfo(HttpServletRequest request){
    return new ModelAndView(Path + "/sysinfo");
  }

}

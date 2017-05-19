package com.minivision.cameraplat.mvc;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minivision.cameraplat.domain.ClientUser;
import com.minivision.cameraplat.param.PageParam;
import com.minivision.cameraplat.service.ClientUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.minivision.cameraplat.domain.SysLog;
import com.minivision.cameraplat.service.SysLogService;

@RestController
public class SysInfoController {

  @Autowired
  private SysLogService sysLogService;

  @Autowired
  private ClientUserService clientUserService;

  @GetMapping("sysinfo")
  public ModelAndView sysInfo() {
    return new ModelAndView("sysmanage/sysinfo");
  }

  @GetMapping(value = "syslog")
  public ModelAndView getSyslog() {
    return new ModelAndView("sysmanage/sysloglist") ;
  }

  @GetMapping(value = "syslog",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Map<String,Object> getSyslog(PageParam pageParam,String modelName,String startTime,String endTime)
      throws ParseException {
    Page<SysLog> pages = sysLogService.findByPage(pageParam,modelName,startTime,endTime);
    List<SysLog> sysLogs = pages.getContent();
    long totalcout = pages.getTotalElements();
    Map<String,Object> map = new HashMap<>();
    map.put("rows",sysLogs);
    map.put("total",totalcout);
    return  map;
  }

  @GetMapping("clientuser")
  public ModelAndView clientUserPage(){
      return new ModelAndView("sysmanage/clientuserlist");
  }

  @GetMapping(value = "clientuser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<ClientUser> userlist(){
    return  clientUserService.findAll();
  }

  @PostMapping("clientuser")
  public String addClientUser(ClientUser clientUser){
    clientUserService.save(clientUser);
    return "success";
  }

  @PatchMapping("clientuser")
    public String updateClientUser(ClientUser clientUser){
      clientUserService.update(clientUser);
      return "success";
  }

  @DeleteMapping("clientuser")
  public String deleteClientUser(ClientUser clientUser){
      clientUserService.delete(clientUser);
      return "success";
  }
}


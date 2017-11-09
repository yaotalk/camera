package com.minivision.cameraplat.rest;

import com.minivision.cameraplat.domain.CameraGroup;
import com.minivision.cameraplat.domain.ClientUser;
import com.minivision.cameraplat.domain.EntranceGuard;
import com.minivision.cameraplat.domain.Region;
import com.minivision.cameraplat.rest.result.RestResult;
import com.minivision.cameraplat.rest.result.system.CameraResult;
import com.minivision.cameraplat.rest.result.system.UserResult;
import com.minivision.cameraplat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
@Api(tags = "SystemApi", value = "System Apis")
public class SystemApi {

  @Autowired
  private RegionService regionService;

  @Autowired
  private CameraService cameraService;

  @Autowired
  private ClientUserService clientUserService;

  @Autowired
  private EntranceService entranceService;

  @Autowired
  private CameraGroupService cameraGroupService;

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemApi.class);

  @GetMapping(value = "regions")
  @ApiOperation(value = "区域查询", notes = "区域查询")
  public RestResult<List<Region>> getRegions() {
    List<Region> list = regionService.findAll();
    return new RestResult<>(list);
  }

  @GetMapping(value = "cameras")
  @ApiOperation(value = "设备信息", notes = "设备信息")
  public RestResult<List<CameraResult>> getCameras() {
    List<CameraResult> list = cameraService.findAllCameraResults();
    return new RestResult<>(list);
  }

  @PostMapping(value = "userlogin")
  @ApiOperation(value = "用户验证", notes = "用户验证")
  public RestResult<UserResult> userLogin(@RequestParam("username") @ApiParam(required = true) String username,
                                          @RequestParam("password") @ApiParam(required = true) String password) {
    UserResult userResult = clientUserService.validateUser(username,password);
    return new RestResult<>(userResult);
  }

  @PostMapping(value = "resetpwd")
  @ApiOperation(value = "重置密码",notes = "重置密码")
  public RestResult<UserResult> reset(@RequestParam("username") @ApiParam(required = true) String username,
                                      @RequestParam("password") @ApiParam(required = true) String password,
                                      @RequestParam("newpassword") @ApiParam(required = true) String newpassword){

    UserResult userResult = clientUserService.validateUser(username,password);
    if(userResult.getStatusCode() != 200){
      return new RestResult<>(userResult);
    }
    else {
      ClientUser user = clientUserService.findByUsername(username);
      user.setPassword(newpassword);
      clientUserService.update(user);
    }
    userResult.setMsg("更改密码成功");
    return new RestResult<>(userResult);
  }

  @GetMapping(value = "entrance")
  @ApiOperation(value = "门禁设备", notes = "门禁设备")
  public RestResult<List<EntranceGuard>> entranceGuards() {
      List<EntranceGuard> entranceGuards = entranceService.findAll();
      return new RestResult<>(entranceGuards);
  }

  @GetMapping(value = "cameraGroups")
  @ApiOperation(value = "摄像机分组", notes = "摄像机分组")
  public RestResult<List<CameraGroup>> cameraGroups() {
    List<CameraGroup> cameraGroups = cameraGroupService.findAll();
    return new RestResult<>(cameraGroups);
  }

}

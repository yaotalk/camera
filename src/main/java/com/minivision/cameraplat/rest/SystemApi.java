package com.minivision.cameraplat.rest;

import com.minivision.cameraplat.domain.ClientUser;
import com.minivision.cameraplat.domain.EntranceGuard;
import com.minivision.cameraplat.domain.Region;
import com.minivision.cameraplat.domain.User;
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
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1", method = RequestMethod.POST)
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

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemApi.class);

  @RequestMapping(value = "regions")
  @ApiOperation(value = "区域查询", notes = "区域查询")
  public RestResult<List<Region>> getRegions() {
    LOGGER.trace("region search at api");
    List<Region> list = regionService.findAll();
    LOGGER.trace("region search at api");
    return new RestResult<>(list);
  }

  @RequestMapping(value = "cameras")
  @ApiOperation(value = "设备信息", notes = "设备信息")
  public RestResult<List<CameraResult>> getCameras() {
    LOGGER.trace("cameras search at api");
    List<CameraResult> list = cameraService.findAllCameraResults();
    LOGGER.trace("cameras search at api");
    return new RestResult<>(list);
  }

  @RequestMapping(value = "userlogin")
  @ApiOperation(value = "用户验证", notes = "用户验证")
  public RestResult<UserResult> userLogin(@RequestParam("username") @ApiParam(required = true) String username,
                                          @RequestParam("password") @ApiParam(required = true) String password) {
    LOGGER.trace("userlogin  at api");
    UserResult userResult = clientUserService.validateUser(username,password);
    LOGGER.trace("userlogin  at api");
    return new RestResult<>(userResult);
  }

  @RequestMapping(value = "resetpwd")
  @ApiOperation(value = "重置密码",notes = "重置密码")
  public RestResult<UserResult> reset(@RequestParam("username") @ApiParam(required = true) String username,
                                      @RequestParam("password") @ApiParam(required = true) String password,
                                      @RequestParam("newpassword") @ApiParam(required = true) String newpassword){

    LOGGER.trace("resetpwd  at api");
    UserResult userResult = clientUserService.validateUser(username,password);
    if(userResult.getStatusCode() != 200){
      return new RestResult<>(userResult);
    }
    else {
      ClientUser user = clientUserService.findByUsername(username);
      user.setPassword(newpassword);
      clientUserService.update(user);
    }
    LOGGER.trace("resetpwd  at api");
    userResult.setMsg("更改密码成功");
    return new RestResult<>(userResult);
  }

  @RequestMapping(value = "entrance")
  @ApiOperation(value = "门禁设备", notes = "门禁设备")
  public RestResult<List<EntranceGuard>> entranceGuards() {
      LOGGER.trace("entrance search  at api");
      List<EntranceGuard> entranceGuards = entranceService.findAll();
      LOGGER.trace("entrance search  at api");
      return new RestResult<>(entranceGuards);
  }

}

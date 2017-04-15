package com.minivision.camaraplat.rest;

import com.minivision.camaraplat.domain.Region;
import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.rest.result.RestResult;
import com.minivision.camaraplat.rest.result.system.CameraResult;
import com.minivision.camaraplat.rest.result.system.UserResult;
import com.minivision.camaraplat.service.CameraService;
import com.minivision.camaraplat.service.RegionService;
import com.minivision.camaraplat.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.ManyToOne;
import java.util.ArrayList;
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
  private UserService userService;

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
    UserResult userResult = userService.validateUser(username,password);
    LOGGER.trace("userlogin  at api");
    return new RestResult<>(userResult);
  }

}

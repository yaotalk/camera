package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.config.ConAnnotation;
import com.minivision.camaraplat.config.RequestDomain;
import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yao on 2017/3/12.
 */
@RestController
@ConAnnotation(modelName = "登陆模块")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView( "/login");
    }


    @ConAnnotation(modelName = "用户登陆")
    @PostMapping("/userlogin")
    public Map<String,String> userlogin(
            @RequestParam(value = "username",defaultValue = "")String username,
            @RequestParam(value = "password",defaultValue = "")String password,
            ModelAndView model){
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isEmpty(username.trim())|| StringUtils.isEmpty(password.trim())){
                map.put("status","error");
                map.put("msg","用户名或密码不能为空");
                return  map;
        }
        User user = userService.loginIn(username,password);
        if(user == null){
            map.put("status","error");
            map.put("msg","用户名或密码错误");
            return map;
        }
        map.put("status","ok");
        map.put("msg","登陆成功");
        RequestDomain.getSession().setAttribute("user",user);
        return  map;
    }


}

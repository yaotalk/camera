package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.config.ConAnnotation;
import com.minivision.camaraplat.config.EncodeConfig;
import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@ConAnnotation(modelName = "登陆模块")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView( "login/login");
    }


    @ConAnnotation(modelName = "用户登陆")
    @PostMapping("/userlogin")
    public Map<String,String> userlogin(
            @RequestParam(value = "username",defaultValue = "")String username,
            @RequestParam(value = "password",defaultValue = "")String password, HttpServletRequest request,HttpServletResponse response){
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isEmpty(username.trim())|| StringUtils.isEmpty(password.trim())){
                map.put("status","error");
                map.put("msg","用户名或密码不能为空");
                return  map;
        }
        password = EncodeConfig.EncodeByMd5(password);
        User user = userService.loginIn(username,password);
        if(user == null){
            map.put("status","error");
            map.put("msg","用户名或密码错误");
            return map;
        }
        map.put("status","ok");
        map.put("msg","登陆成功");
        request.getSession().setAttribute("user",user);
        request.getSession().setMaxInactiveInterval(30*60);
        if(user.isAutologin()){
            Cookie cookie = new Cookie("username",username);
            cookie.setPath("/");
            cookie.setMaxAge(30*60*60*24);
            response.addCookie(cookie);
            cookie = new Cookie("password",password);
            cookie.setPath("/");
            cookie.setMaxAge(30*60*60*24);
            response.addCookie(cookie);
        }
        return  map;
    }

    @GetMapping("/logout")
    public String logOut(HttpServletRequest request,HttpServletResponse response){
        request.getSession().invalidate();
         return "success";
    }



}

package com.minivision.camaraplat.config.interceptor;

import com.minivision.camaraplat.config.EncodeConfig;
import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User user = (User) request.getSession().getAttribute("user");
			if (user != null) {
				if(request.getRequestURI().endsWith("loginset"))
			  {
					user = userService.findOne(user.getId());
					request.getSession().setAttribute("user",user);
				}
				if(request.getRequestURI().endsWith("login")) {
					response.sendRedirect("/faceset/getFaceset");
				}
				return true;
			}
		Cookie[] cookies = request.getCookies();
		if(cookies !=null){
			String username = "";
			String password = "";
				for(Cookie cookie:cookies){
						  if("username".equals(cookie.getName())){
						  		  	  username = cookie.getValue();
							}
							if("password".equals(cookie.getName())){
								        password = cookie.getValue();
							}
				}
				User cookieUser = userService.loginIn(username, password);
			  if(cookieUser != null && cookieUser.isAutologin()){
					request.getSession(true).setAttribute("user",cookieUser);
					request.getSession(true).setMaxInactiveInterval(30*60);
					if(request.getRequestURI().endsWith("login") || request.getRequestURI().endsWith("/")) {
							response.sendRedirect("/faceset/getFaceset");
							return true;
					}
					response.sendRedirect(request.getContextPath() + request.getRequestURI());
					return true;
				}
		}
		if(request.getRequestURI().endsWith("login")){
			return  true;
		}
	  response.sendRedirect(request.getContextPath() + "/user/login");
		return false;
		//return true;
	}

}

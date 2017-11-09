package com.minivision.faceplat.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AppKeyInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String appKey = request.getParameter("appKey");
		String appSecret = request.getParameter("appSecret");
		return access(appKey, appSecret);
	}

	private boolean access(String appKey, String appSecret) {
		return true;
	}


}

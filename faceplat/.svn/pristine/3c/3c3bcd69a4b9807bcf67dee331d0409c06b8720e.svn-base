package com.minivision.faceplat.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AppKeyInterceptor extends HandlerInterceptorAdapter {
	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");

	private static final Logger LOGGER = LoggerFactory.getLogger(AppKeyInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		long beginTime = System.currentTimeMillis();
		startTimeThreadLocal.set(beginTime);

		String appKey = request.getParameter("appKey");
		String appSecret = request.getParameter("appSecret");
		return access(appKey, appSecret);
	}

	private boolean access(String appKey, String appSecret) {
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long endTime = System.currentTimeMillis();
		long beginTime = startTimeThreadLocal.get();
		long consumeTime = endTime - beginTime;
		LOGGER.info("{} consume {} millis", request.getRequestURI(), consumeTime);
		super.afterCompletion(request, response, handler, ex);
	}

}

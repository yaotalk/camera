package com.minivision.faceplat;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.minivision.faceplat.rest.result.RestResult;

@RestControllerAdvice
public class BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public RestResult<?> handleControllerException(HttpServletRequest request, Exception ex) {
		
		LOGGER.info("Request:{} with '{}', error:", request.getRequestURI(), request.getQueryString(), ex);
        return new RestResult<>(ex);
    }
}

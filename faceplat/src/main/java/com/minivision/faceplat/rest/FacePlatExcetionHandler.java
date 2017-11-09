package com.minivision.faceplat.rest;

import javax.servlet.http.HttpServletRequest;

import com.minivision.faceplat.rest.result.ResultError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.minivision.faceplat.rest.result.RestResult;
import com.minivision.faceplat.service.ex.FacePlatException;

@RestControllerAdvice
public class FacePlatExcetionHandler {
  @ExceptionHandler(FacePlatException.class)
  @ResponseBody
  public RestResult<ResultError> handleFacePlatException(HttpServletRequest request, FacePlatException ex) {
      ResultError re = new ResultError();
      re.setErrorMessage(ex.getMessage());
      re.setErrorCode(ex.getErrCode());
      re.setPath(request.getRequestURI());
      re.setTimestamp(System.currentTimeMillis());
      return new RestResult<>(re);
  }
  
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseBody
  public RestResult<ResultError> handleArgException(HttpServletRequest request, IllegalArgumentException ex) {
      ResultError re = new ResultError();
      re.setErrorMessage(ex.getMessage());
      re.setErrorCode(400);
      re.setPath(request.getRequestURI());
      re.setTimestamp(System.currentTimeMillis());
      return new RestResult<>(re);
  }
  
/*  @ExceptionHandler(Exception.class)
  @ResponseBody
  public RestResult<?> handleControllerException(HttpServletRequest request, Exception ex) {
      return new RestResult<>(ex);
  }*/
}

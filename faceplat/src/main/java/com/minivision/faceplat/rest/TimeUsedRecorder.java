package com.minivision.faceplat.rest;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minivision.faceplat.rest.result.RestResult;

@Aspect
@Component
public class TimeUsedRecorder {
  
  private static final Logger logger = LoggerFactory.getLogger("API_TRACE_");
    
    @Autowired
    private ObjectMapper om;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object timeuse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        if(obj instanceof RestResult<?>){
          RestResult<?> res = (RestResult<?>) obj;
          res.setTimeUsed((int) (System.currentTimeMillis() - start));
          String name = proceedingJoinPoint.getSignature().getName();
          Object[] args = proceedingJoinPoint.getArgs();
          try{
            logger.trace("method: {}", name);
            logger.trace("args: {}", Arrays.toString(args));
            logger.info("result: {}", om.writeValueAsString(res));
          }catch (Exception e){
            logger.error("", e);
          }
          
        };
        return obj;
    }

}

package com.minivision.camaraplat.config;

import com.minivision.camaraplat.domain.SysLog;
import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by yao on 2017/3/12.
 * AOP
 * 日志记录
 */

@Aspect
@Component
//@SessionAttributes("user")
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("execution(* com.minivision.camaraplat.mvc..*.*(..))")
    public void ControllerAspect(){

    }


    @After(value = "ControllerAspect()")
    public void LogAfter(JoinPoint joinPoint) throws Exception{
        User user = (User)RequestDomain.getSession().getAttribute("user");
        String targetname = joinPoint.getSignature().getName();
        Class<?> target = joinPoint.getTarget().getClass();
        String pModename =  joinPoint.getTarget().getClass().getAnnotation(ConAnnotation.class).modelName();
        Class<?> targetclass = Class.forName(target.getName());
        Method[] methods = targetclass.getMethods();
        //Object[] args = joinPoint.getArgs();
         for(Method method : methods){
             if(method.getName().equals(targetname) && method.getAnnotation(ConAnnotation.class)!=null){
                 if(user != null) {
                     System.out.println("target user is ___________" + user.getUsername());
                     String modelname = method.getAnnotation(ConAnnotation.class).modelName();
                     String opration = method.getAnnotation(ConAnnotation.class).opration();
                     String ip = RequestDomain.getRequest().getRemoteAddr();
                     SysLog sysLog = new SysLog(user,ip, pModename + "->" + modelname, opration, Calendar.getInstance().getTime());
                     sysLogService.save(sysLog);
                     System.out.println("target pmodename is  _____________" + pModename);
                     System.out.println("target modelname is  ________________" + method.getAnnotation(ConAnnotation.class).modelName());
                     System.out.println("target opration is ______________________" + method.getAnnotation(ConAnnotation.class).opration());
                     break;
                 }
             }
         }

    }


}

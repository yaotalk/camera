package com.minivision.camaraplat.config;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by yao on 2017/3/12.
 */
public class RequestDomain {
  public static HttpSession getSession() {
    HttpSession session = null;
    try {
      session = getRequest().getSession();

    } catch (Exception e) {
    }
    return session;
  }

  public static HttpServletRequest getRequest() {
    ServletRequestAttributes attrs =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attrs.getRequest();
  }

}

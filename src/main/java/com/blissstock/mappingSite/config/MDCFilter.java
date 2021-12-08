package com.blissstock.mappingSite.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MDCFilter implements Filter {

    


  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(
    ServletRequest req,
    ServletResponse resp,
    FilterChain chain
  )
    throws IOException, ServletException {
    //add access url to log
    if (req instanceof HttpServletRequest) {
    
      String url = ((HttpServletRequest) req).getRequestURL().toString();
      //String queryString = ((HttpServletRequest) req).getQueryString();
      MDC.put("accessURL", url);
    }
    
    //add login user email to the log
    Authentication authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();

    if (authentication != null) {
      MDC.put("email", authentication.getName());
    }
    try {
      chain.doFilter(req, resp);
    } finally {
      if (authentication != null) {
        MDC.remove("email");
      }
    }
  }

  @Override
  public void destroy() {}
}

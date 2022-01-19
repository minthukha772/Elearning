package com.blissstock.mappingSite.config;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static Logger logger = LoggerFactory.getLogger(MyAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        logger.info("invalid login");
        logger.info("request.getContextPath() {}",request.getContextPath());
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String redirectURL = "/login?error";
        logger.info("before redirectURL {}",redirectURL);

       if(exception.getMessage().toLowerCase().contains("suspend")) {
            redirectURL+="=suspended";
        }

        logger.info("redirectURL {}",redirectURL);



        super.setDefaultFailureUrl(redirectURL);
        super.onAuthenticationFailure(request, response, exception);

    }

}

package com.wru.onthi.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandler implements
        org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("ADMIN".equals(auth.getAuthority())) {
                httpServletResponse.sendRedirect("/admin/home");
            } else if ("USER".equals(auth.getAuthority())) {
                httpServletResponse.sendRedirect("/");
            } else if ("MANAGER".equals(auth.getAuthority())) {
                httpServletResponse.sendRedirect("/lesson/list-lesson");
            }
        }
    }
}

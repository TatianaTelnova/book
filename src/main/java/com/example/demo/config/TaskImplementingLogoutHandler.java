package com.example.demo.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TaskImplementingLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res, Authentication authentication) {
        System.out.println("logout!!!!");

    }
}

package com.sproutt.eussyaeussyaapi.api.security;

import com.sproutt.eussyaeussyaapi.api.security.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(tokenKey);

        if (token == null || !jwtHelper.isUsable(secretKey, token)) {
            throw new InvalidTokenException();
        }

        return true;
    }
}

package com.tutorial.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.spring.common.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    ApiResponse<Void> errorResponse = ApiResponse.error("로그인 해야합니다.");
    String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

    response.getWriter().write(jsonResponse);
  }
}

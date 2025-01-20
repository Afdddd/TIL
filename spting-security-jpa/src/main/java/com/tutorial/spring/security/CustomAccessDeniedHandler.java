package com.tutorial.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.spring.common.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    ApiResponse<Object> errorResponse = ApiResponse.error("권한이 없습니다.");
    String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

    response.getWriter().write(jsonResponse);

  }
}

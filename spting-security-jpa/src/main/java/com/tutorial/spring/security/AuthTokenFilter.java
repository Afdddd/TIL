package com.tutorial.spring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 토큰을 검증하고 사용자 인증 정보를 설정하는 필터.
 *
 * Spring Security의 OncePerRequestFilter를 상속.
 * 각 요청마다 JWT 토큰을 파싱하고 유효성을 검사하여 인증을 설정.
 */
public class AuthTokenFilter extends OncePerRequestFilter {

  @Autowired
  private JwtTokenProvider jwtProvider;

  @Autowired
  private CustomUserDetailService customUserDetailService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try{
      String jwt = parseJwt(request);// 요청에서 JWT 추출
      if (jwt != null && jwtProvider.validateToken(jwt)) { // JWT 유효성 검증
        String username = jwtProvider.getUsernameFromToken(jwt); // username 추출
        List roles = jwtProvider.getRolesFromToken(jwt); // roles 추출


        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }catch(Exception e){
      System.out.println("Cannot set user authentication : " + e);
    }
    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }
}

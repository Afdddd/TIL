package com.tutorial.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 역할: Spring Security 설정 클래스.
 *
 * JWT 인증 필터를 추가하고, 인증/인가 규칙을 정의합니다.
 * 주요 설정:
 * CSRF 비활성화.
 * 특정 경로(예: /api/auth/**)를 모든 사용자에게 허용.
 * 나머지 요청은 인증 필요.
 * JWT 필터를 인증 필터 이전에 추가.
 */

@Configuration
public class WebSecurityConfig {

  @Autowired
  CustomUserDetailService customUserDetailService;

  @Autowired
  private CustomAccessDeniedHandler customAccessDeniedHandler;

  @Autowired
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Updated configuration for Spring Security 6.x
    http
        .csrf(csrf -> csrf.disable()) // Disable CSRF
        .cors(cors -> cors.disable()) // Disable CORS (or configure if needed)
        .exceptionHandling(ex ->
            ex.authenticationEntryPoint(customAuthenticationEntryPoint)
        )
        .exceptionHandling(ex->
            ex.accessDeniedHandler(customAccessDeniedHandler)
        )
        .sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/api/auth/**").permitAll()  // 인증 없이 접근 가능
                .requestMatchers("/api/admin/**").hasRole("ADMIN")  // ADMIN만 접근 가능
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")  // USER와 ADMIN 모두 접근 가능
                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
        );
    // Add the JWT Token filter before the UsernamePasswordAuthenticationFilter
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }



}

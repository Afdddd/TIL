package com.tutorial.spring.controller;

import com.tutorial.spring.common.ApiResponse;
import com.tutorial.spring.common.ErrorCode;
import com.tutorial.spring.entity.User;
import com.tutorial.spring.repository.UserRepository;
import com.tutorial.spring.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  UserRepository userRepository;
  @Autowired
  PasswordEncoder encoder;
  @Autowired
  JwtTokenProvider jwtTokenProvider;


  @PostMapping("/signin")
  public String authenticateUser(@RequestBody User user) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
    );
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    return jwtTokenProvider.generateToken(userDetails.getUsername());
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody User user) {

    if(userRepository.existsByUsername(user.getUsername())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ErrorCode.USER_NOT_FOUND.getMessage()));
    }


    User newUser = User.builder()
        .username(user.getUsername())
        .password(encoder.encode(user.getPassword()))
        .build();

    userRepository.save(newUser);
    return ResponseEntity.ok(ApiResponse.success(newUser, "생성 완료"));
  }
}

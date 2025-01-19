package com.tutorial.spring.security;

import com.tutorial.spring.repository.UserRepository;
import com.tutorial.spring.entity.User;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 데이터베이스에서 사용자 정보를 로드하는 서비스.
 *
 * UserDetailsService 인터페이스 구현.
 * loadUserByUsername 메서드에서 사용자 정보를 로드하여 Spring Security의 UserDetails 객체로 반환.
 */

@Service
public class CustomUserDetailService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        Collections.emptyList());
  }
}

package com.tutorial.spring.security;


import com.tutorial.spring.entity.Role;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 역할: JWT 토큰 생성 및 검증.
 *
 * 토큰 생성: generateToken 메서드로 사용자 이름을 기반으로 JWT 생성.
 * 토큰 검증: validateToken 메서드로 토큰의 유효성을 검증.
 * 사용자 이름 추출: getUsernameFromToken 메서드로 토큰에서 사용자 이름 파싱.
 */

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;
  @Value("${jwt.expiration}")
  private int jwtExpirationMs;


  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }


  // 토큰 생성
  public String generateToken(String username, List<Role> roles) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

    // 역할 정보를 문자열 리스트로 변환
    List<String> roleNames = roles.stream()
        .map(Role::getRoleName)
        .collect(Collectors.toList());

    return Jwts.builder()
        .subject(username)
        .claim("roles", roleNames)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(this.getSigningKey())
        .compact();
  }

  // 토큰에서 사용자 이름 추출
  public String getUsernameFromToken(String token) {
    return Jwts.parser()
        .verifyWith(this.getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public List getRolesFromToken(String token) {
    return Jwts.parser()
        .verifyWith(this.getSigningKey())// 서명 키 설정
        .build()
        .parseSignedClaims(token) // JWT 파싱
        .getPayload()
        .get("roles", List.class); // "roles" 클레임에서 역할 정보 추출
  }

  public boolean validateToken(String token) {
    try{
      Jwts.parser()
          .verifyWith(this.getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }


}

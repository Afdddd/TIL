package com.tutorial.spring.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다.");


  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}

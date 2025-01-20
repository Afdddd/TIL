package com.tutorial.spring.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
  private String status; // SUCCESS or ERROR
  private T data;        // Actual data
  private String message; // User-friendly message

  public ApiResponse(String status, T data, String message) {
    this.status = status;
    this.data = data;
    this.message = message;
  }

  // Success response
  public static <T> ApiResponse<T> success(T data, String message) {
    return new ApiResponse<>("SUCCESS", data, message);
  }

  // Error response
  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>("ERROR", null, message);
  }

  // Getters and setters (optional if using Lombok)
}


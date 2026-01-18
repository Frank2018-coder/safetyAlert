package com.safetynet.alerts.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, Object>> notFound(NotFoundException ex) {
    log.error("Not found: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error("NOT_FOUND", ex.getMessage()));
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<Map<String, Object>> conflict(ConflictException ex) {
    log.error("Conflict: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error("CONFLICT", ex.getMessage()));
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
  public ResponseEntity<Map<String, Object>> badRequest(Exception ex) {
    log.error("Bad request", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("BAD_REQUEST", ex.getMessage() == null ? "Bad request" : ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> generic(Exception ex) {
    log.error("Unhandled error", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error("INTERNAL_ERROR", "Unexpected error"));
  }

  private static Map<String, Object> error(String code, String message) {
    Map<String, Object> m = new HashMap<>();
    m.put("code", code);
    m.put("message", message);
    return m;
  }
}

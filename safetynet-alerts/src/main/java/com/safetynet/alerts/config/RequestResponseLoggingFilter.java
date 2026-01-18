package com.safetynet.alerts.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
  private static final Logger log = LogManager.getLogger(RequestResponseLoggingFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    try {
      filterChain.doFilter(wrappedRequest, wrappedResponse);
    } catch (Exception e) {
      log.error("Request failed: {} {}", request.getMethod(), request.getRequestURI(), e);
      throw e;
    } finally {
      int status = wrappedResponse.getStatus();
      String query = request.getQueryString();
      String path = request.getRequestURI() + (query == null ? "" : "?" + query);

      String reqBody = bodyToString(wrappedRequest.getContentAsByteArray());
      String resBody = bodyToString(wrappedResponse.getContentAsByteArray());

      if (status >= 200 && status < 400) {
        log.info("{} {} => {} | reqBody={} | resBody={}", request.getMethod(), path, status, safe(reqBody), safe(resBody));
      } else {
        log.error("{} {} => {} | reqBody={} | resBody={}", request.getMethod(), path, status, safe(reqBody), safe(resBody));
      }

      wrappedResponse.copyBodyToResponse();
    }
  }

  private static String bodyToString(byte[] bytes) {
    if (bytes == null || bytes.length == 0) return "";
    return new String(bytes, StandardCharsets.UTF_8);
  }

  private static String safe(String s) {
    if (s == null) return "";
    // Keep logs readable
    String trimmed = s.replaceAll("\\s+", " ").trim();
    return trimmed.length() > 500 ? trimmed.substring(0, 500) + "..." : trimmed;
  }
}

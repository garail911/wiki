package tech.itpark.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenExtractor implements Middleware {
  @Override
  public boolean process(HttpServletRequest request, HttpServletResponse response) {
    final var token = request.getHeader("Authorization");
    request.setAttribute("TOKEN", token);
    return false;
  }
}

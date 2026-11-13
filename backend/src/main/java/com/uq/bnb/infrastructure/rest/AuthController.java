package com.uq.bnb.infrastructure.rest;

import com.uq.bnb.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> request, 
            HttpServletResponse response) {
        // For frontend testing purposes, create a token with email from request or default
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            email = "test@example.com";
        }
        
        String token = jwtService.generateToken(email);
        addTokenInCookie(response, token);
        
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody Map<String, String> request, 
            HttpServletResponse response) {
        // For frontend testing purposes, create a token with email from request or default
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            email = "test@example.com";
        }
        
        String token = jwtService.generateToken(email);
        addTokenInCookie(response, token);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Clear the token cookie
        Cookie cookie = new Cookie("access_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
    
    private void addTokenInCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 24 hours
        
        response.addCookie(cookie);
    }
}
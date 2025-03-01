package com.bnpl.infrastructure.controller;

import com.bnpl.dto.AuthRequest;
import com.bnpl.dto.AuthResponse;
import com.bnpl.dto.ApiResponse;
import com.bnpl.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * Endpoint to authenticate users and generate JWT tokens.
     * @param authRequest - Contains username and password.
     * @return JWT token wrapped in a response.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication.getName());

        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .message("Login successful")
                .data(AuthResponse.builder().token(token).build())
                .build());
    }

    /**
     * Endpoint to validate if the JWT token is still valid.
     * @return Success message if the token is valid.
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateToken() {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Token is valid")
                .data("Valid token")
                .build());
    }
}

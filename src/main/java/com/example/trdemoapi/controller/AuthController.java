package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.LoginReq;
import com.example.trdemoapi.dto.LoginResp;
import com.example.trdemoapi.service.AuthenticationService;
import com.example.trdemoapi.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/auth")
@Tag(name="Auth", description="the endpoints for authentication")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary="Authenticate user", description="Returns with a JWT token and when will it expire")
    @PostMapping("/login")
    public ResponseEntity<LoginResp> authenticate(@Valid @RequestBody LoginReq input) {
        var authenticatedUser = authenticationService.authenticate(input);
        var jwtToken = jwtService.generateToken(authenticatedUser);
        var loginResponse = new LoginResp(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}

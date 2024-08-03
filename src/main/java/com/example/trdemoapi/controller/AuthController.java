package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.LoginReq;
import com.example.trdemoapi.dto.LoginResp;
import com.example.trdemoapi.dto.SignUpReq;
import com.example.trdemoapi.service.AuthenticationService;
import com.example.trdemoapi.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignUpReq input) {
        var registeredUser = authenticationService.signup(input);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginReq input) {
        var authenticatedUser = authenticationService.authenticate(input);

        var jwtToken = jwtService.generateToken(authenticatedUser);

        var loginResponse = new LoginResp(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}

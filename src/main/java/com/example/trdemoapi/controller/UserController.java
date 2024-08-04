package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.PasswordChangeReq;
import com.example.trdemoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name="User", description="the endpoints for all authenticated users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary="Details of current user", description="Returns with id, name and email of current user")
    @GetMapping("/me")
    public ResponseEntity<?> getDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var currentUser = userService.loadUserByUsername(authentication.getName());

        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<?> changePassword(@RequestParam PasswordChangeReq passwordChangeRequest) {
        return ResponseEntity.ok("password change");
    }
}

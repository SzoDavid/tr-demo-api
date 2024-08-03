package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.PasswordChangeReq;
import com.example.trdemoapi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/me")
    public ResponseEntity<?> getDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var currentUser = (User) authentication.getPrincipal();
        // TODO
        // Hibernate.initialize(result.getIngredients());
        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<?> changePassword(@RequestParam PasswordChangeReq passwordChangeRequest) {
        return ResponseEntity.ok("password change");
    }
}

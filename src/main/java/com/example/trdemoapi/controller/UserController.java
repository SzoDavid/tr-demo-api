package com.example.trdemoapi.controller;

import com.example.trdemoapi.requestModel.PasswordChangeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> getDetails() {
        return ResponseEntity.ok("details");
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<?> changePassword(@RequestParam PasswordChangeRequest passwordChangeRequest) {
        return ResponseEntity.ok("password change");
    }
}

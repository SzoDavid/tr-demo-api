package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.PasswordChangeReq;
import com.example.trdemoapi.model.User;
import com.example.trdemoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
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
    public ResponseEntity<User> getDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = userService.loadUserByEmail(authentication.getName());

        return ResponseEntity.ok(currentUser);
    }

    @Operation(summary="Change password", description="If the old password is correct, updates the users password to the new one")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Password changed successfully", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "string"),
                    examples = @ExampleObject(value = "Password changed successfully")
            )),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"errors\":[\"Current password is incorrect.\"]}")
            ))
    })
    @PutMapping("/me/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChangeReq passwordChangeRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        userService.changePassword(authentication.getName(), passwordChangeRequest);

        return ResponseEntity.ok("Password changed successfully");
    }
}

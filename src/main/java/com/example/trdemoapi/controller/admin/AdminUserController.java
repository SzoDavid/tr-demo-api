package com.example.trdemoapi.controller.admin;

import com.example.trdemoapi.dto.CreateUserReq;
import com.example.trdemoapi.dto.SuccessResp;
import com.example.trdemoapi.dto.UpdateUserRolesReq;
import com.example.trdemoapi.model.User;
import com.example.trdemoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/admin/users")
@Tag(name="Admin - User", description="the endpoints for authenticated admins to manage users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary="All users", description="Returns with the details of all users.")
    @GetMapping("/")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id,asc") String[] sortBy) {

        var users = userService.getUsersPage(PageRequest.of(offset, pageSize,
                Sort.by(Sort.Order.by(sortBy[0]).with(Sort.Direction.fromString(sortBy[1])))));

        return ResponseEntity.ok(users);
    }

    @Operation(summary="Get user by id", description="Returns with the details of the user with the given id.")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        var user = userService.loadUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @Operation(summary="Create user", description="Returns with the user created.")
    @PostMapping("/")
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserReq request) {
        var user = userService.createUser(request);
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary="Update user roles", description="Updates the user's roles with the given list.")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserRoles(@PathVariable Long id, @Valid @RequestBody UpdateUserRolesReq request) {
        var user = userService.loadUserById(id);
        var response = userService.updateUserRoles(user, request.getRoles());

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary="Delete user", description="Deletes the user with the given id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResp> deleteUser(@PathVariable Long id) {
        var user = userService.loadUserById(id);
        userService.deleteUser(user);

        return ResponseEntity.ok().body(new SuccessResp(true, "User deleted successfully"));
    }
}

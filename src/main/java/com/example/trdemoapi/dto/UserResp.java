package com.example.trdemoapi.dto;

import com.example.trdemoapi.model.User;

import java.util.ArrayList;
import java.util.List;

public record UserResp(Long id, String name, String email, List<String> roles) {
    public static UserResp fromUser(User user) {
        var roles = new ArrayList<String>();
        for (var role : user.getRoles()) {
            roles.add(role.getName());
        }

        return new UserResp(user.getId(), user.getName(), user.getEmail(), roles);
    }
}

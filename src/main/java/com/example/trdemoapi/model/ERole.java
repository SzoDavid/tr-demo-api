package com.example.trdemoapi.model;

import lombok.Getter;

@Getter
public enum ERole {
    ADMIN("ADMIN"),
    TEACHER("TEACHER"),
    STUDENT("STUDENT");

    private final String name;

    ERole(String name) {
        this.name = name;
    }

    public String getNameWithPrefix() {
        return "ROLE_" + name;
    }
}

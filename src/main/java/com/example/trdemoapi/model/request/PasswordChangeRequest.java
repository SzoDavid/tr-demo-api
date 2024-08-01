package com.example.trdemoapi.model.request;

public record PasswordChangeRequest(String oldPassword, String newPassword) {
}

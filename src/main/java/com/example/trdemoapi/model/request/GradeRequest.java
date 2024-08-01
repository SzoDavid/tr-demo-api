package com.example.trdemoapi.model.request;

import com.example.trdemoapi.model.User;

public record GradeRequest(User student, Integer grade) {
}

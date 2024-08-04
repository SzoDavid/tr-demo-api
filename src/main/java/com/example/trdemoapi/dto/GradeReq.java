package com.example.trdemoapi.dto;

import com.example.trdemoapi.model.User;

public record GradeReq(User student, Integer grade) {
}

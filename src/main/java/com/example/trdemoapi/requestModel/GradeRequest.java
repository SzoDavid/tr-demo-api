package com.example.trdemoapi.requestModel;

import com.example.trdemoapi.model.User;

public record GradeRequest(User student, Integer grade) {
}

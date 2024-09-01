package com.example.trdemoapi.dto.timetable;

import java.util.List;
import java.util.Map;

public record TimetableResp(Map<Short, List<TimetableElement>> timetable) {
}

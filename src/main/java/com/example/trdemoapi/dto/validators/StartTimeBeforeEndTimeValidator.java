package com.example.trdemoapi.dto.validators;

import com.example.trdemoapi.dto.Schedule;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StartTimeBeforeEndTimeValidator implements ConstraintValidator<StartTimeBeforeEndTime, Schedule> {
    @Override
    public boolean isValid(Schedule schedule, ConstraintValidatorContext constraintValidatorContext) {
        if (schedule == null) {
            return true;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(schedule.getStartTime(), timeFormatter);
        LocalTime endTime = LocalTime.parse(schedule.getEndTime(), timeFormatter);

        return startTime.isBefore(endTime);
    }
}

package com.example.trdemoapi.dto;

import com.example.trdemoapi.dto.validators.StartTimeBeforeEndTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
@StartTimeBeforeEndTime
public class Schedule {
    @NotNull(message="Day of course is required")
    @Range(min=1, max=7, message="Day must be between 1 and 5")
    private short day;

    @NotNull(message="Start time is required")
    @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "Start time must be in HH:mm format")
    private String startTime;

    @NotNull(message="End time is required")
    @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "End time must be in HH:mm format")
    private String endTime;
}

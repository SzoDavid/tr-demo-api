package com.example.trdemoapi.dto;

import com.example.trdemoapi.dto.validators.StartTimeBeforeEndTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Range;

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
    @Pattern(regexp = "^((0[89]|1[0-9]):[0-5][0-9]|20:00)$", message = "Start time must be in HH:mm format and must be " +
            "between 8:00 and 20:00")
    private String startTime;

    @NotNull(message="End time is required")
    @Pattern(regexp = "^((0[89]|1[0-9]):[0-5][0-9]|20:00)$", message = "End time must be in HH:mm format and must be " +
            "between 8:00 and 20:00")
    private String endTime;
}

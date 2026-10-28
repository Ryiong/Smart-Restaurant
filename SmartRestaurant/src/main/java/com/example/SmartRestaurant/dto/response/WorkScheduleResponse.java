package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.WorkScheduleStatus;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WorkScheduleResponse {
    private Long id;
    private LocalDate date;
    private EmploymentResponse employmentResponse;
    private ShiftResponse shift;
    private WorkScheduleStatus status;
}

package com.example.SmartRestaurant.dto.request;

import com.example.SmartRestaurant.common.enums.WorkScheduleAttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkScheduleActionRequest {
    private Long id;
    private WorkScheduleAttendanceStatus status;
    private String rejectReason;
}

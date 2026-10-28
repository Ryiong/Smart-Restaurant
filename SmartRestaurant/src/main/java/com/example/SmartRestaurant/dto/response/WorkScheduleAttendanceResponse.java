package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.WorkScheduleAttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WorkScheduleAttendanceResponse {
    private Long id;
    private LocalDate date;
    private EmploymentResponse employment;
    private ShiftResponse shift;
    private List<AttendanceResponse> attendances;
    private WorkScheduleAttendanceStatus attendanceStatus;
}

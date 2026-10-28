package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.AttendanceType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttendanceResponse {
    private Long id;
    private AttendanceType type;
    private UserResponse user;
    private WorkScheduleResponse workSchedule;
    private UserResponse viewedBy;
    private LocalDateTime createdAt;
    private LocalDateTime viewedAt;

}

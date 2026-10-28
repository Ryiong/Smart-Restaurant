package com.example.SmartRestaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendanceRequest {
    private Long shopId;
    private Long workScheduleId;
    private LocalDateTime qrExpiredAt;
    private Double longitude;
    private Double latitude;
}

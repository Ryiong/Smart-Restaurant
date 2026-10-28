package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.ShiftStatus;
import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShiftResponse {
    private Long id;
    private String name;
    private ShiftStatus status;
    private LocalTime startTime;
    private LocalTime endTime;
}

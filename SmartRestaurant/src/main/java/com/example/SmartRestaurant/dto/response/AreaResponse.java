package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.AreaStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AreaResponse {
    private Long id;
    private String name;
    private String description;
    private AreaStatus status;
}

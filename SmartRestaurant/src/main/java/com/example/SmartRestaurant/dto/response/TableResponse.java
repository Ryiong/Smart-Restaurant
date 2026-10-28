package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.TableOrderStatus;
import com.example.SmartRestaurant.common.enums.TableStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TableResponse {
    private Long id;
    private String name;
    private boolean qrEnabled;
    private TableOrderStatus orderStatus;
    private TableStatus status;
}

package com.example.SmartRestaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmploymentRequest {
    private RegisterRequest registerRequest;
    private Long salary;
    private LocalDate workFrom;
    private List<Long> roleIds;
}

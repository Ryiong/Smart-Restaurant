package com.example.SmartRestaurant.service.employment;

import com.example.SmartRestaurant.dto.request.EmploymentRehireRequest;
import com.example.SmartRestaurant.dto.request.EmploymentRequest;
import com.example.SmartRestaurant.dto.response.EmploymentResponse;
import com.example.SmartRestaurant.service.base.ParentResourceBaseService;

public interface EmploymentService extends ParentResourceBaseService<EmploymentRequest, EmploymentResponse, Long> {


    EmploymentResponse createFromExistingUser(Long shopId, Long userId, EmploymentRehireRequest request);

}

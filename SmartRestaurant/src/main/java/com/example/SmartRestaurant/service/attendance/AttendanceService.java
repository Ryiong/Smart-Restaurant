package com.example.SmartRestaurant.service.attendance;

import com.example.SmartRestaurant.dto.request.AttendanceRequest;
import com.example.SmartRestaurant.dto.response.AttendanceResponse;


public interface AttendanceService {

    AttendanceResponse checkAttendance(AttendanceRequest request);

}

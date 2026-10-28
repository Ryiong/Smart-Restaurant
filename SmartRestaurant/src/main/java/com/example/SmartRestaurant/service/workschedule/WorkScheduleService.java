package com.example.SmartRestaurant.service.workschedule;

import com.example.SmartRestaurant.dto.request.WorkScheduleActionRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleExplainRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleUpdateRequest;
import com.example.SmartRestaurant.dto.response.WorkScheduleAttendanceResponse;
import com.example.SmartRestaurant.dto.response.WorkScheduleResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WorkScheduleService {
    List<WorkScheduleResponse> create(Long shopId, LocalDate date, List<WorkScheduleRequest> requests);

    WorkScheduleResponse update(Long shopId, Long workScheduleId, WorkScheduleUpdateRequest request);

    void delete(Long workScheduleId);

    Set<WorkScheduleResponse> getByShopIdAndDate(Long shopId, LocalDate date);

    List<WorkScheduleAttendanceResponse> getAllByShopIdAndEmploymentIdFromDateToDate(Long shopId,
                                                                                     Long employmentId,
                                                                                     LocalDate startDate,
                                                                                     LocalDate endDate);

    List<WorkScheduleAttendanceResponse> updateAttendanceStatus(Long shopId,
                                                                Long employmentId,
                                                                List<WorkScheduleActionRequest> requests);

    void explain(Long shopId, Long workScheduleId, WorkScheduleExplainRequest explainRequest);
}

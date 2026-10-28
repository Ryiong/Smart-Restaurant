package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.WorkScheduleStatus;
import com.example.SmartRestaurant.entity.WorkScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkScheduleEntity, Long> {

    Set<WorkScheduleEntity> findAllByDateAndEmployment_Shop_IdAndStatus(LocalDate date, Long shopId, WorkScheduleStatus scheduled);

    WorkScheduleEntity findByIdAndAndEmployment_Shop_IdAndStatus(Long workScheduleId, Long shopId, WorkScheduleStatus status);

    @Query("select distinct ws from WorkScheduleEntity ws " +
            "left join fetch ws.attendances " +
            "where ws.employment.id=:employmentId " +
            "and ws.date between :startDate and :endDate " +
            "order by ws.date")
    List<WorkScheduleEntity> findAllByEmploymentIdFromDateToDate(Long employmentId,
                                                                 LocalDate startDate,
                                                                 LocalDate endDate);

    List<WorkScheduleEntity> findAllByIdInAndEmploymentId(Set<Long> workScheduleRequestIds, Long employmentId);

}

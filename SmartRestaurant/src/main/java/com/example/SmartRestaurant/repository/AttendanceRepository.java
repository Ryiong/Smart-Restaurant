package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    Set<AttendanceEntity> findAllByWorkScheduleId(Long workScheduleId);

}

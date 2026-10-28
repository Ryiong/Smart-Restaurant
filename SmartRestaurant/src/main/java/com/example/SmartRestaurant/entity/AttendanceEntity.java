package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.AttendanceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_attendance")
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceType type;

    @ManyToOne
    @JoinColumn(name = "work_schedule_id")
    private WorkScheduleEntity workSchedule;

    @Column(nullable = false, updatable = false)
    private Double longitude;

    @Column(nullable = false, updatable = false)
    private Double latitude;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

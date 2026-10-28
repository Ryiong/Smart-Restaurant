package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.WorkScheduleAttendanceStatus;
import com.example.SmartRestaurant.common.enums.WorkScheduleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_work_schedule")
public class WorkScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "employment_id", nullable = false)
    private EmploymentEntity employment;//lịch này cho ai

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private ShiftEntity shift;//ca làm nào

    @Enumerated
    @Column(nullable = false)
    private WorkScheduleAttendanceStatus attendanceStatus;//duyệt công bằng cái này

    private String rejectReason;


    @ManyToOne
    private UserEntity reviewedBy;

    private LocalDateTime reviewedAt;

    private String explaination;//giải trình

    private LocalDateTime explainedAt;

    @OneToMany(mappedBy = "workSchedule")
    private List<AttendanceEntity> attendances;

    @Enumerated
    @Column(nullable = false)
    private WorkScheduleStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

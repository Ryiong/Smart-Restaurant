package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.OTPStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_otp")
public class OTPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private LocalDateTime expiredAt;
    private LocalDateTime lastSentAt;
    private Integer resendCount;
    private LocalDateTime lockedUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OTPStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private UserEntity user;
}

package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.ShopStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_shop")
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;//mục này chỉ phục vụ mục đích hiển thị cho người dùng thôi

    @Column
    private Double longitude;//đây mới là mục phục vụ cho việc xác định trên bản đồ để xác nhận công

    @Column
    private Double latitude;

    @Column
    private Integer attendanceRadius;

    private String phoneNumber;
    @Column(length = 500)
    private String statusReason;//không lưu lịch sử trạng thái tài khoản làm gì
    //, sau này nếu cần thì sẽ thêm bảng cho mục này sau

    @Enumerated(EnumType.STRING)
    private ShopStatus status;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<RoleEntity> roles;

    @OneToMany(mappedBy = "shop")
    private List<InventoryDocumentEntity> inventoryDocuments;

    @OneToMany(mappedBy = "shop")
    private List<EmploymentEntity> employments;

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

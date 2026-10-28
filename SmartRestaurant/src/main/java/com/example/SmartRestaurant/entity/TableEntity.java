package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.TableOrderStatus;
import com.example.SmartRestaurant.common.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_table")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

    private String qrImageUrl;

    private boolean qrEnabled;

    @Enumerated(EnumType.STRING)
    private TableOrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private AreaEntity area;

    @OneToMany(mappedBy = "table")
    private List<OrderEntity> orders;

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

package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.OrderItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_order_item")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "delete_user_id")
    private UserEntity deletedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;//order nào

    @Column(length = 100, updatable = false)
    private String note;//snapshot

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;//sản phẩm gì

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;//ai đặt món

    @Column(nullable = false, updatable = false)
    private String productName;//snapshot

    @Column(nullable = false, updatable = false)
    private BigDecimal price;//snapshot

    @Column(nullable = false, updatable = false)
    private Integer quantity;//snapshot

    @Column(nullable = false, updatable = false)
    private BigDecimal totalPrice;

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

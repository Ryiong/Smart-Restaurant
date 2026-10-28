package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.InventoryDocumentStatus;
import com.example.SmartRestaurant.common.enums.InventoryDocumentType;
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
@Table(name = "tbl_inventory_document")
public class InventoryDocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String rejectReason;//chỉ dùng với status CANCELLED

    @Column(length = 500, updatable = false)
    private String note;//chỉ dùng khi tạo phiếu

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryDocumentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryDocumentStatus status;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private UserEntity createdBy;//nhân viên nào tạo phiếu này

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private UserEntity reviewedBy;//chủ hay quản lý xử lý phiếu này

    @Column
    private LocalDateTime reviewedAt;

    @OneToMany(mappedBy = "inventoryDocument")
    private List<InventoryDocumentItemEntity> items;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}

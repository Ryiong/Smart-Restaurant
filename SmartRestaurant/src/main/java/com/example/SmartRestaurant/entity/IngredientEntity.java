package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.IngredientStatus;
import com.example.SmartRestaurant.common.enums.IngredientType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_ingredient")
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientStatus status;

    @Column(length = 20, nullable = false)
    private String unit;

    @Column(nullable = false)
    private BigDecimal currentStock;

    @Column(nullable = false)
    private BigDecimal minStock;

    @Column(nullable = false)
    private BigDecimal yieldRate;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    @OneToMany(mappedBy = "ingredient")
    private List<RecipeIngredientEntity> recipeIngredientEntities;

    @OneToMany(mappedBy = "ingredient")
    private List<InventoryDocumentItemEntity> items;

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

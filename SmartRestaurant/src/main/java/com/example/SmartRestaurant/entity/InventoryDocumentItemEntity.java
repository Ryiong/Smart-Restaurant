package com.example.SmartRestaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_inventory_document_item")
public class InventoryDocumentItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "inventory_document_id", nullable = false, updatable = false)
    private InventoryDocumentEntity inventoryDocument;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false, updatable = false)
    private IngredientEntity ingredient;
}

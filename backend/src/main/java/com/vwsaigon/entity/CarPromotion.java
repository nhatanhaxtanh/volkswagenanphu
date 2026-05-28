package com.vwsaigon.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car_promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_model_id", unique = true, nullable = false)
    private Long carModelId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "promotion_items", joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "item")
    @Builder.Default
    private List<String> items = new ArrayList<>();

    private String validUntil;

    @Builder.Default
    private boolean active = true;
}

package com.vwsaigon.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "car_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private String category;
    private Long price;
    private String priceDisplay;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String engine;
    private String power;
    private String torque;
    private Integer seats;
    private String fuelType;
    private String transmission;
    private String imageUrl;
    private String videoUrl;

    @ElementCollection
    @CollectionTable(name = "car_model_images", joinColumns = @JoinColumn(name = "car_model_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Builder.Default
    private int sortOrder = 0;

    @Builder.Default
    private boolean featured = false;

    @Builder.Default
    private boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

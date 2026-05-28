package com.vwsaigon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hero_slides")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeroSlide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @Column(nullable = false)
    private String heading;

    private String sub;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private String imageUrl;
    private String videoUrl;

    @Builder.Default
    private int sortOrder = 0;

    @Builder.Default
    private boolean active = true;
}

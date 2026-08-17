package com.vwsaigon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/** Ảnh chèn vào phần "Giới thiệu" của một dòng xe, kèm chú thích. */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescriptionImage {

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "caption")
    private String caption;
}

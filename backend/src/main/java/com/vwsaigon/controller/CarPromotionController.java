package com.vwsaigon.controller;

import com.vwsaigon.entity.CarPromotion;
import com.vwsaigon.service.CarModelService;
import com.vwsaigon.service.CarPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CarPromotionController {

    private final CarPromotionService promotionService;
    private final CarModelService carModelService;

    @GetMapping("/api/models/{slug}/promotion")
    public ResponseEntity<CarPromotion> getBySlug(@PathVariable String slug) {
        Long modelId = carModelService.getBySlug(slug).getId();
        return promotionService.findByCarModelId(modelId)
                .filter(CarPromotion::isActive)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/admin/models/{id}/promotion")
    public ResponseEntity<CarPromotion> getForAdmin(@PathVariable Long id) {
        return promotionService.findByCarModelId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/admin/models/{id}/promotion")
    public ResponseEntity<CarPromotion> save(
            @PathVariable Long id,
            @RequestBody CarPromotion promo) {
        return ResponseEntity.ok(promotionService.save(id, promo));
    }
}

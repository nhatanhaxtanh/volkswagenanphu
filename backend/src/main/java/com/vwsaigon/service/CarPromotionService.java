package com.vwsaigon.service;

import com.vwsaigon.entity.CarPromotion;
import com.vwsaigon.repository.CarPromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarPromotionService {
    private final CarPromotionRepository repository;

    public Optional<CarPromotion> findByCarModelId(Long carModelId) {
        return repository.findByCarModelId(carModelId);
    }

    public CarPromotion save(Long carModelId, CarPromotion data) {
        CarPromotion existing = repository.findByCarModelId(carModelId)
                .orElse(CarPromotion.builder().carModelId(carModelId).build());
        existing.setTitle(data.getTitle());
        existing.setDescription(data.getDescription());
        existing.setItems(data.getItems() != null ? data.getItems() : new ArrayList<>());
        existing.setValidUntil(data.getValidUntil());
        existing.setActive(data.isActive());
        return repository.save(existing);
    }
}

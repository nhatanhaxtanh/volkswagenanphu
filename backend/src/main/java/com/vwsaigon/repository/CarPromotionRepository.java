package com.vwsaigon.repository;

import com.vwsaigon.entity.CarPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarPromotionRepository extends JpaRepository<CarPromotion, Long> {
    Optional<CarPromotion> findByCarModelId(Long carModelId);
}

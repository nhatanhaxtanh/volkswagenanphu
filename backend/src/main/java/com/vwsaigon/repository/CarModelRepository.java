package com.vwsaigon.repository;

import com.vwsaigon.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    Optional<CarModel> findBySlug(String slug);
    List<CarModel> findByActiveTrue();
    List<CarModel> findByActiveTrueAndFeaturedTrue();
}

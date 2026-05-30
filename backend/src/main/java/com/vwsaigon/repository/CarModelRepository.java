package com.vwsaigon.repository;

import com.vwsaigon.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    Optional<CarModel> findBySlug(String slug);

    @Query("SELECT m FROM CarModel m WHERE m.active = true ORDER BY m.sortOrder ASC, m.createdAt ASC")
    List<CarModel> findActiveOrdered();

    @Query("SELECT m FROM CarModel m ORDER BY m.sortOrder ASC, m.createdAt ASC")
    List<CarModel> findAllOrdered();

    @Query("SELECT m FROM CarModel m WHERE m.active = true AND m.featured = true ORDER BY m.sortOrder ASC, m.createdAt ASC")
    List<CarModel> findFeaturedOrdered();
}

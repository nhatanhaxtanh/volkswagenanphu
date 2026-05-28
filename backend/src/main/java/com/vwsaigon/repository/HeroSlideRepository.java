package com.vwsaigon.repository;

import com.vwsaigon.entity.HeroSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HeroSlideRepository extends JpaRepository<HeroSlide, Long> {
    List<HeroSlide> findByActiveTrueOrderBySortOrderAsc();
    List<HeroSlide> findAllByOrderBySortOrderAsc();
}

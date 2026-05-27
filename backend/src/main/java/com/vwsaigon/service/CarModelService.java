package com.vwsaigon.service;

import com.vwsaigon.entity.CarModel;
import com.vwsaigon.repository.CarModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository repository;

    public List<CarModel> getAll() {
        return repository.findByActiveTrue();
    }

    public List<CarModel> getAllForAdmin() {
        return repository.findAll();
    }

    public List<CarModel> getFeatured() {
        return repository.findByActiveTrueAndFeaturedTrue();
    }

    public CarModel getBySlug(String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Car model not found: " + slug));
    }

    public CarModel create(CarModel model) {
        return repository.save(model);
    }

    public CarModel update(Long id, CarModel updated) {
        CarModel existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car model not found: " + id));
        existing.setName(updated.getName());
        existing.setSlug(updated.getSlug());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setPriceDisplay(updated.getPriceDisplay());
        existing.setShortDescription(updated.getShortDescription());
        existing.setDescription(updated.getDescription());
        existing.setEngine(updated.getEngine());
        existing.setPower(updated.getPower());
        existing.setTorque(updated.getTorque());
        existing.setSeats(updated.getSeats());
        existing.setFuelType(updated.getFuelType());
        existing.setTransmission(updated.getTransmission());
        existing.setImageUrl(updated.getImageUrl());
        existing.setFeatured(updated.isFeatured());
        existing.setActive(updated.isActive());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

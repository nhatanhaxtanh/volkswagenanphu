package com.vwsaigon.service;

import com.vwsaigon.entity.CarModel;
import com.vwsaigon.repository.CarModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository repository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

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
        existing.setVideoUrl(updated.getVideoUrl());
        existing.setFeatured(updated.isFeatured());
        existing.setActive(updated.isActive());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public CarModel uploadThumbnail(Long id, MultipartFile file, String baseUrl) {
        CarModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car model not found: " + id));

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : ".jpg";
        if (!java.util.List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext)) {
            throw new RuntimeException("Invalid file type");
        }
        if (file.getSize() > 2L * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 2MB limit");
        }

        try {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir, "thumbnails");
            java.nio.file.Files.createDirectories(uploadPath);
            String filename = java.util.UUID.randomUUID() + ext;
            java.nio.file.Files.copy(file.getInputStream(), uploadPath.resolve(filename), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            model.setImageUrl(baseUrl + "/api/uploads/thumbnails/" + filename);
            return repository.save(model);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}

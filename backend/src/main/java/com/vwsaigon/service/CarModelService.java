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
        return repository.findActiveOrdered();
    }

    public List<CarModel> getAllForAdmin() {
        return repository.findAllOrdered();
    }

    public List<CarModel> getFeatured() {
        return repository.findFeaturedOrdered();
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
        existing.setPriceEstimated(updated.isPriceEstimated());
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
        if (updated.getHighlights() != null) {
            if (existing.getHighlights() == null) existing.setHighlights(new java.util.ArrayList<>());
            existing.getHighlights().clear();
            updated.getHighlights().stream()
                    .filter(h -> h != null && h.getTitle() != null && !h.getTitle().isBlank())
                    .forEach(existing.getHighlights()::add);
        }
        existing.setSortOrder(updated.getSortOrder());
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

    public CarModel addGalleryImage(Long id, MultipartFile file, String baseUrl) {
        CarModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car model not found: " + id));
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : ".jpg";
        if (!java.util.List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext))
            throw new RuntimeException("Invalid file type");
        if (file.getSize() > 2L * 1024 * 1024)
            throw new RuntimeException("File size exceeds 2MB limit");
        try {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir, "gallery");
            java.nio.file.Files.createDirectories(uploadPath);
            String filename = java.util.UUID.randomUUID() + ext;
            java.nio.file.Files.copy(file.getInputStream(), uploadPath.resolve(filename), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            if (model.getImages() == null) model.setImages(new java.util.ArrayList<>());
            model.getImages().add(baseUrl + "/api/uploads/gallery/" + filename);
            return repository.save(model);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public CarModel removeGalleryImage(Long id, String imageUrl) {
        CarModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car model not found: " + id));
        if (model.getImages() != null) model.getImages().remove(imageUrl);
        return repository.save(model);
    }
}

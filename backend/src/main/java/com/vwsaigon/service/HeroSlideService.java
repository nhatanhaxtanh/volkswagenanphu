package com.vwsaigon.service;

import com.vwsaigon.entity.HeroSlide;
import com.vwsaigon.repository.HeroSlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeroSlideService {

    private final HeroSlideRepository repository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public List<HeroSlide> getActive() {
        return repository.findByActiveTrueOrderBySortOrderAscIdAsc();
    }

    public List<HeroSlide> getAll() {
        return repository.findAllByOrderBySortOrderAscIdAsc();
    }

    public HeroSlide create(HeroSlide slide) {
        return repository.save(slide);
    }

    public HeroSlide update(Long id, HeroSlide updated) {
        HeroSlide existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hero slide not found: " + id));
        existing.setLabel(updated.getLabel());
        existing.setHeading(updated.getHeading());
        existing.setSub(updated.getSub());
        existing.setDescription(updated.getDescription());
        existing.setImageUrl(updated.getImageUrl());
        existing.setVideoUrl(updated.getVideoUrl());
        existing.setSortOrder(updated.getSortOrder());
        existing.setActive(updated.isActive());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public HeroSlide uploadImage(Long id, MultipartFile file, String baseUrl) {
        HeroSlide slide = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hero slide not found: " + id));

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : ".jpg";
        if (!java.util.List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext)) {
            throw new RuntimeException("Invalid file type. Allowed: .jpg, .jpeg, .png, .webp");
        }
        if (file.getSize() > 5L * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 5MB limit");
        }

        try {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir, "hero");
            java.nio.file.Files.createDirectories(uploadPath);
            String filename = java.util.UUID.randomUUID() + ext;
            java.nio.file.Files.copy(file.getInputStream(), uploadPath.resolve(filename),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            slide.setImageUrl(baseUrl + "/api/uploads/hero/" + filename);
            slide.setVideoUrl(null);
            return repository.save(slide);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public HeroSlide uploadVideo(Long id, MultipartFile file, String baseUrl) {
        HeroSlide slide = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hero slide not found: " + id));

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "video.mp4";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : ".mp4";
        if (!java.util.List.of(".mp4", ".webm").contains(ext)) {
            throw new RuntimeException("Invalid file type. Allowed: .mp4, .webm");
        }
        if (file.getSize() > 50L * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 50MB limit");
        }

        try {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir, "hero");
            java.nio.file.Files.createDirectories(uploadPath);
            String filename = java.util.UUID.randomUUID() + ext;
            java.nio.file.Files.copy(file.getInputStream(), uploadPath.resolve(filename),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            slide.setVideoUrl(baseUrl + "/api/uploads/hero/" + filename);
            slide.setImageUrl(null);
            return repository.save(slide);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}

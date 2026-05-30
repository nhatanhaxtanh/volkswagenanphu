package com.vwsaigon.service;

import com.vwsaigon.entity.HandoverPhoto;
import com.vwsaigon.repository.HandoverPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HandoverPhotoService {

    private final HandoverPhotoRepository repository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public List<HandoverPhoto> getActive() {
        return repository.findActiveOrdered();
    }

    public List<HandoverPhoto> getAll() {
        return repository.findAllOrdered();
    }

    public HandoverPhoto create(HandoverPhoto photo) {
        return repository.save(photo);
    }

    public HandoverPhoto update(Long id, HandoverPhoto updated) {
        HandoverPhoto existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found: " + id));
        existing.setCaption(updated.getCaption());
        existing.setSortOrder(updated.getSortOrder());
        existing.setActive(updated.isActive());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public HandoverPhoto uploadImage(Long id, MultipartFile file, String baseUrl) {
        HandoverPhoto photo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found: " + id));

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : ".jpg";
        if (!java.util.List.of(".jpg", ".jpeg", ".webp").contains(ext))
            throw new RuntimeException("Invalid file type");
        if (file.getSize() > 2L * 1024 * 1024)
            throw new RuntimeException("File size exceeds 2MB limit");

        try {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir, "handover");
            java.nio.file.Files.createDirectories(uploadPath);
            String filename = java.util.UUID.randomUUID() + ext;
            java.nio.file.Files.copy(file.getInputStream(), uploadPath.resolve(filename),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            photo.setImageUrl(baseUrl + "/api/uploads/handover/" + filename);
            return repository.save(photo);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}

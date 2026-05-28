package com.vwsaigon.service;

import com.vwsaigon.entity.NewsPost;
import com.vwsaigon.repository.NewsPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsPostService {

    private final NewsPostRepository repository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public List<NewsPost> getPublished() {
        return repository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    public List<NewsPost> getAll() {
        return repository.findAll();
    }

    public NewsPost getBySlug(String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Not found: " + slug));
    }

    public NewsPost create(NewsPost post) {
        return repository.save(post);
    }

    public NewsPost update(Long id, NewsPost updated) {
        NewsPost existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found: " + id));
        existing.setTitle(updated.getTitle());
        existing.setSlug(updated.getSlug());
        existing.setCategory(updated.getCategory());
        existing.setImageUrl(updated.getImageUrl());
        existing.setExcerpt(updated.getExcerpt());
        existing.setContent(updated.getContent());
        existing.setPublished(updated.isPublished());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public NewsPost uploadImage(Long id, MultipartFile file, String baseUrl) {
        NewsPost post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found: " + id));
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : ".jpg";
        if (!java.util.List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext))
            throw new RuntimeException("Invalid file type");
        if (file.getSize() > 2L * 1024 * 1024)
            throw new RuntimeException("File size exceeds 2MB");
        try {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir, "news");
            java.nio.file.Files.createDirectories(uploadPath);
            String filename = java.util.UUID.randomUUID() + ext;
            java.nio.file.Files.copy(file.getInputStream(), uploadPath.resolve(filename),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            post.setImageUrl(baseUrl + "/api/uploads/news/" + filename);
            return repository.save(post);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}

package com.vwsaigon.service;

import com.vwsaigon.entity.NewsPost;
import com.vwsaigon.repository.NewsPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsPostService {

    private final NewsPostRepository repository;

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
}

package com.vwsaigon.controller;

import com.vwsaigon.entity.NewsPost;
import com.vwsaigon.service.NewsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsPostService service;

    @GetMapping("/api/news")
    public List<NewsPost> getPublished() {
        return service.getPublished();
    }

    @GetMapping("/api/news/{slug}")
    public ResponseEntity<NewsPost> getBySlug(@PathVariable String slug) {
        try {
            return ResponseEntity.ok(service.getBySlug(slug));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/admin/news")
    public List<NewsPost> getAll() {
        return service.getAll();
    }

    @PostMapping("/api/admin/news")
    public ResponseEntity<NewsPost> create(@RequestBody NewsPost post) {
        return ResponseEntity.ok(service.create(post));
    }

    @PutMapping("/api/admin/news/{id}")
    public ResponseEntity<NewsPost> update(@PathVariable Long id, @RequestBody NewsPost post) {
        try {
            return ResponseEntity.ok(service.update(id, post));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/admin/news/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/admin/news/{id}/image")
    public ResponseEntity<NewsPost> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        String proto = request.getHeader("X-Forwarded-Proto");
        String host = request.getHeader("Host");
        if (proto == null) proto = request.getScheme();
        if (host == null) host = request.getServerName() + (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");
        String baseUrl = proto + "://" + host;
        return ResponseEntity.ok(service.uploadImage(id, file, baseUrl));
    }
}

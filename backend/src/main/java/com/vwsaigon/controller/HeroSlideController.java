package com.vwsaigon.controller;

import com.vwsaigon.entity.HeroSlide;
import com.vwsaigon.service.HeroSlideService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HeroSlideController {

    private final HeroSlideService service;

    // Public endpoint
    @GetMapping("/api/hero-slides")
    public List<HeroSlide> getActive() {
        return service.getActive();
    }

    // Admin endpoints
    @GetMapping("/api/admin/hero-slides")
    public List<HeroSlide> getAll() {
        return service.getAll();
    }

    @PostMapping("/api/admin/hero-slides")
    public ResponseEntity<HeroSlide> create(@RequestBody HeroSlide slide) {
        return ResponseEntity.ok(service.create(slide));
    }

    @PutMapping("/api/admin/hero-slides/{id}")
    public ResponseEntity<HeroSlide> update(@PathVariable Long id, @RequestBody HeroSlide slide) {
        try {
            return ResponseEntity.ok(service.update(id, slide));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/admin/hero-slides/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/admin/hero-slides/{id}/image")
    public ResponseEntity<HeroSlide> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        String scheme = request.getHeader("X-Forwarded-Proto") != null
                ? request.getHeader("X-Forwarded-Proto") : request.getScheme();
        String host = request.getHeader("Host") != null
                ? request.getHeader("Host") : request.getServerName() +
                  (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");
        return ResponseEntity.ok(service.uploadImage(id, file, scheme + "://" + host));
    }

    @PostMapping("/api/admin/hero-slides/{id}/video")
    public ResponseEntity<HeroSlide> uploadVideo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        String scheme = request.getHeader("X-Forwarded-Proto") != null
                ? request.getHeader("X-Forwarded-Proto") : request.getScheme();
        String host = request.getHeader("Host") != null
                ? request.getHeader("Host") : request.getServerName() +
                  (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");
        return ResponseEntity.ok(service.uploadVideo(id, file, scheme + "://" + host));
    }
}

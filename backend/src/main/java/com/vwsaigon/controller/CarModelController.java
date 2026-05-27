package com.vwsaigon.controller;

import com.vwsaigon.entity.CarModel;
import com.vwsaigon.service.CarModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarModelController {

    private final CarModelService service;

    // Public endpoints
    @GetMapping("/api/models")
    public List<CarModel> getAll() {
        return service.getAll();
    }

    @GetMapping("/api/models/featured")
    public List<CarModel> getFeatured() {
        return service.getFeatured();
    }

    @GetMapping("/api/models/{slug}")
    public ResponseEntity<CarModel> getBySlug(@PathVariable String slug) {
        try {
            return ResponseEntity.ok(service.getBySlug(slug));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin endpoints
    @GetMapping("/api/admin/models")
    public List<CarModel> getAllForAdmin() {
        return service.getAllForAdmin();
    }

    @PostMapping("/api/admin/models")
    public ResponseEntity<CarModel> create(@RequestBody CarModel model) {
        return ResponseEntity.ok(service.create(model));
    }

    @PutMapping("/api/admin/models/{id}")
    public ResponseEntity<CarModel> update(@PathVariable Long id, @RequestBody CarModel model) {
        try {
            return ResponseEntity.ok(service.update(id, model));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/admin/models/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.vwsaigon.controller;

import com.vwsaigon.entity.HandoverPhoto;
import com.vwsaigon.service.HandoverPhotoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HandoverPhotoController {

    private final HandoverPhotoService service;

    @GetMapping("/api/handover-photos")
    public List<HandoverPhoto> getActive() {
        return service.getActive();
    }

    @GetMapping("/api/admin/handover-photos")
    public List<HandoverPhoto> getAll() {
        return service.getAll();
    }

    @PostMapping("/api/admin/handover-photos")
    public ResponseEntity<HandoverPhoto> create(@RequestBody HandoverPhoto photo) {
        return ResponseEntity.ok(service.create(photo));
    }

    @PutMapping("/api/admin/handover-photos/{id}")
    public ResponseEntity<HandoverPhoto> update(@PathVariable Long id, @RequestBody HandoverPhoto photo) {
        try {
            return ResponseEntity.ok(service.update(id, photo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/admin/handover-photos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/admin/handover-photos/{id}/image")
    public ResponseEntity<HandoverPhoto> uploadImage(
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

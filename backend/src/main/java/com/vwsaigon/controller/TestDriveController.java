package com.vwsaigon.controller;

import com.vwsaigon.dto.TestDriveDto;
import com.vwsaigon.entity.TestDriveRequest;
import com.vwsaigon.service.TestDriveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestDriveController {

    private final TestDriveService service;

    @PostMapping("/api/test-drive")
    public ResponseEntity<?> submit(@Valid @RequestBody TestDriveDto dto) {
        TestDriveRequest saved = service.submit(dto);
        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công", "id", saved.getId()));
    }

    @GetMapping("/api/admin/test-drives")
    public List<TestDriveRequest> getAll() {
        return service.getAll();
    }

    @PutMapping("/api/admin/test-drives/{id}/status")
    public ResponseEntity<TestDriveRequest> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(service.updateStatus(id, body.get("status")));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/admin/test-drives/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

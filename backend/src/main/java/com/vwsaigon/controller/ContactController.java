package com.vwsaigon.controller;

import com.vwsaigon.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Map<String, String>> submit(@RequestBody Map<String, String> body) {
        String fullName = body.getOrDefault("fullName", "").trim();
        String phone   = body.getOrDefault("phone", "").trim();
        String email   = body.getOrDefault("email", "").trim();
        String message = body.getOrDefault("message", "").trim();

        if (fullName.isBlank() || phone.isBlank() || message.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Vui lòng điền đầy đủ thông tin"));
        }

        emailService.sendContactNotification(fullName, phone, email.isBlank() ? null : email, message);
        return ResponseEntity.ok(Map.of("message", "Tin nhắn đã được gửi"));
    }
}

package com.vwsaigon.controller;

import com.vwsaigon.dto.ChangePasswordDto;
import com.vwsaigon.dto.LoginDto;
import com.vwsaigon.entity.Admin;
import com.vwsaigon.repository.AdminRepository;
import com.vwsaigon.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            String token = tokenProvider.generateToken(dto.getUsername());
            return ResponseEntity.ok(Map.of("token", token, "username", dto.getUsername()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Sai tên đăng nhập hoặc mật khẩu"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of("username", auth.getName()));
    }

    @PostMapping("/admin/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminRepository.findByUsername(auth.getName())
                .orElseThrow();
        if (!passwordEncoder.matches(dto.getOldPassword(), admin.getPasswordHash())) {
            return ResponseEntity.status(400).body(Map.of("message", "Mật khẩu cũ không đúng"));
        }
        admin.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        adminRepository.save(admin);
        return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công"));
    }
}

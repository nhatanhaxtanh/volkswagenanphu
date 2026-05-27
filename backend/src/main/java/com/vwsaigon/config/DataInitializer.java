package com.vwsaigon.config;

import com.vwsaigon.entity.Admin;
import com.vwsaigon.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String defaultUsername;

    @Value("${app.admin.password:Admin@2024!}")
    private String defaultPassword;

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            Admin admin = Admin.builder()
                    .username(defaultUsername)
                    .passwordHash(passwordEncoder.encode(defaultPassword))
                    .build();
            adminRepository.save(admin);
            log.info("Created default admin account: {}", defaultUsername);
        }
    }
}

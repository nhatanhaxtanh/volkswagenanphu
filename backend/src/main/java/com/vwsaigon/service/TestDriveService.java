package com.vwsaigon.service;

import com.vwsaigon.dto.TestDriveDto;
import com.vwsaigon.entity.CarModel;
import com.vwsaigon.entity.TestDriveRequest;
import com.vwsaigon.repository.CarModelRepository;
import com.vwsaigon.repository.TestDriveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDriveService {

    private final TestDriveRepository repository;
    private final CarModelRepository carModelRepository;
    private final EmailService emailService;

    public TestDriveRequest submit(TestDriveDto dto) {
        String modelName = null;
        Long modelId = null;

        if (dto.getModelId() != null && !dto.getModelId().isBlank()) {
            try {
                modelId = Long.parseLong(dto.getModelId());
                modelName = carModelRepository.findById(modelId)
                        .map(CarModel::getName)
                        .orElse(dto.getModelName());
            } catch (NumberFormatException ignored) {}
        }
        if (modelName == null) modelName = dto.getModelName();

        TestDriveRequest request = TestDriveRequest.builder()
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .preferredDate(dto.getPreferredDate())
                .preferredTime(dto.getPreferredTime())
                .modelId(modelId)
                .modelName(modelName)
                .notes(dto.getNotes())
                .build();

        TestDriveRequest saved = repository.save(request);

        emailService.sendTestDriveNotification(saved);
        emailService.sendConfirmationToCustomer(saved);

        return saved;
    }

    public List<TestDriveRequest> getAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public TestDriveRequest updateStatus(Long id, String status) {
        TestDriveRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        request.setStatus(TestDriveRequest.Status.valueOf(status));
        return repository.save(request);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

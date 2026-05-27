package com.vwsaigon.repository;

import com.vwsaigon.entity.TestDriveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestDriveRepository extends JpaRepository<TestDriveRequest, Long> {
    List<TestDriveRequest> findAllByOrderByCreatedAtDesc();
}

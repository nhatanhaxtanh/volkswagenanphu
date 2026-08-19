package com.vwsaigon.repository;

import com.vwsaigon.entity.TestDriveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TestDriveRepository extends JpaRepository<TestDriveRequest, Long> {
    List<TestDriveRequest> findAllByOrderByCreatedAtDesc();

    /**
     * Đếm số đơn đăng ký lái thử theo từng dòng xe, trả về [modelId, count].
     * Bỏ qua đơn không chọn xe cụ thể. Đơn đã huỷ không được tính.
     */
    @Query("""
            SELECT t.modelId, COUNT(t)
            FROM TestDriveRequest t
            WHERE t.modelId IS NOT NULL AND t.status <> com.vwsaigon.entity.TestDriveRequest.Status.CANCELLED
            GROUP BY t.modelId
            """)
    List<Object[]> countGroupedByModelId();
}

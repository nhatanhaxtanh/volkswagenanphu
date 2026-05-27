package com.vwsaigon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TestDriveDto {

    @NotBlank(message = "Họ tên không được trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được trống")
    @Pattern(regexp = "^[0-9+\\s\\-]{9,15}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private String email;

    @NotBlank(message = "Ngày lái thử không được trống")
    private String preferredDate;

    @NotBlank(message = "Giờ lái thử không được trống")
    private String preferredTime;

    private String modelId;
    private String modelName;
    private String notes;
}

package com.vwsaigon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, message = "Mật khẩu phải ít nhất 8 ký tự")
    private String newPassword;
}

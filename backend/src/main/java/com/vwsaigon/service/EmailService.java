package com.vwsaigon.service;

import com.vwsaigon.entity.TestDriveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.notification-to}")
    private String notificationEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendContactNotification(String fullName, String phone, String email, String message) {
        try {
            var msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(notificationEmail);
            helper.setSubject("[VW An Phú] Tin nhắn liên hệ mới - " + fullName);
            helper.setText("""
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                  <div style="background: #000; padding: 20px; text-align: center;">
                    <h1 style="color: #fff; font-size: 18px; margin: 0; letter-spacing: 3px;">VW AN PHÚ</h1>
                  </div>
                  <div style="padding: 30px; border: 1px solid #e5e5e5;">
                    <h2 style="color: #000; margin-top: 0;">Tin nhắn liên hệ mới</h2>
                    <table style="width: 100%%; border-collapse: collapse;">
                      <tr><td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; color: #666; width: 140px;">Họ tên</td>
                          <td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; font-weight: bold;">%s</td></tr>
                      <tr><td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; color: #666;">Điện thoại</td>
                          <td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0;"><a href="tel:%s">%s</a></td></tr>
                      <tr><td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; color: #666;">Email</td>
                          <td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0;">%s</td></tr>
                      <tr><td style="padding: 8px 0; color: #666; vertical-align: top;">Nội dung</td>
                          <td style="padding: 8px 0;">%s</td></tr>
                    </table>
                  </div>
                </div>
                """.formatted(fullName, phone, phone, email != null ? email : "—", message), true);
            mailSender.send(msg);
            log.info("Contact notification sent from: {}", fullName);
        } catch (Exception e) {
            log.error("Failed to send contact email", e);
        }
    }

    @Async
    public void sendTestDriveNotification(TestDriveRequest request) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(notificationEmail);
            helper.setSubject("[VW An Phú] Đăng ký lái thử mới - " + request.getFullName());
            helper.setText(buildNotificationHtml(request), true);
            mailSender.send(message);
            log.info("Test drive notification sent for: {}", request.getFullName());
        } catch (Exception e) {
            log.error("Failed to send notification email", e);
        }
    }

    @Async
    public void sendConfirmationToCustomer(TestDriveRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) return;
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(request.getEmail());
            helper.setSubject("[Volkswagen An Phú] Xác nhận đăng ký lái thử");
            helper.setText(buildConfirmationHtml(request), true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send confirmation email to customer", e);
        }
    }

    private String buildNotificationHtml(TestDriveRequest r) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                  <div style="background: #000; padding: 20px; text-align: center;">
                    <h1 style="color: #fff; font-size: 18px; margin: 0; letter-spacing: 3px;">VW AN PHÚ</h1>
                  </div>
                  <div style="padding: 30px; border: 1px solid #e5e5e5;">
                    <h2 style="color: #000; margin-top: 0;">Đăng ký lái thử mới</h2>
                    <table style="width: 100%%; border-collapse: collapse;">
                      <tr><td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; color: #666; width: 140px;">Họ tên</td>
                          <td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; font-weight: bold;">%s</td></tr>
                      <tr><td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; color: #666;">Điện thoại</td>
                          <td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0;"><a href="tel:%s">%s</a></td></tr>
                      <tr><td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; color: #666;">Email</td>
                          <td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0;">%s</td></tr>
                      <tr><td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; color: #666;">Dòng xe</td>
                          <td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0;">%s</td></tr>
                      <tr><td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0; color: #666;">Ngày / Giờ</td>
                          <td style="padding: 8px 0; border-bottom: 1px solid #f0f0f0;">%s lúc %s</td></tr>
                      <tr><td style="padding: 8px 0; color: #666;">Ghi chú</td>
                          <td style="padding: 8px 0;">%s</td></tr>
                    </table>
                    <div style="margin-top: 24px; text-align: center;">
                      <a href="http://localhost:3000/admin/test-drives" style="background: #000; color: #fff; padding: 12px 24px; text-decoration: none; font-size: 13px; letter-spacing: 1px;">XEM TRONG TRANG QUẢN TRỊ</a>
                    </div>
                  </div>
                </div>
                """.formatted(
                r.getFullName(), r.getPhone(), r.getPhone(),
                r.getEmail() != null && !r.getEmail().isBlank() ? r.getEmail() : "—",
                r.getModelName() != null ? r.getModelName() : "Chưa chọn",
                r.getPreferredDate() != null && !r.getPreferredDate().isBlank() ? r.getPreferredDate() : "Chuyên viên xác nhận",
                r.getPreferredTime() != null && !r.getPreferredTime().isBlank() ? r.getPreferredTime() : "—",
                r.getNotes() != null && !r.getNotes().isBlank() ? r.getNotes() : "—"
        );
    }

    private String buildConfirmationHtml(TestDriveRequest r) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                  <div style="background: #000; padding: 20px; text-align: center;">
                    <h1 style="color: #fff; font-size: 18px; margin: 0; letter-spacing: 3px;">VW AN PHÚ</h1>
                  </div>
                  <div style="padding: 30px; border: 1px solid #e5e5e5;">
                    <h2 style="color: #000; margin-top: 0;">Đăng ký lái thử thành công!</h2>
                    <p style="color: #555;">Xin chào <strong>%s</strong>,</p>
                    <p style="color: #555;">Chúng tôi đã nhận được yêu cầu lái thử của bạn. Thông tin đăng ký:</p>
                    <div style="background: #f9f9f9; padding: 16px; margin: 16px 0;">
                      <p style="margin: 4px 0;"><strong>Dòng xe:</strong> %s</p>
                      <p style="margin: 4px 0;"><strong>Ngày lái thử:</strong> %s</p>
                      <p style="margin: 4px 0;"><strong>Giờ:</strong> %s</p>
                    </div>
                    <p style="color: #555;">Chuyên viên tư vấn sẽ liên hệ với bạn trong vòng <strong>24 giờ</strong> để xác nhận lịch hẹn.</p>
                    <p style="color: #555;">Nếu cần hỗ trợ gấp, vui lòng gọi: <strong><a href="tel:0981058232">098 105 8232</a></strong></p>
                    <p style="color: #888; font-size: 13px; margin-top: 24px;">Volkswagen An Phú — 507C Võ Nguyên Giáp, An Khánh, Thủ Đức, TP.HCM</p>
                  </div>
                </div>
                """.formatted(
                r.getFullName(),
                r.getModelName() != null ? r.getModelName() : "Chưa chọn",
                r.getPreferredDate() != null && !r.getPreferredDate().isBlank() ? r.getPreferredDate() : "Chuyên viên sẽ liên hệ xác nhận",
                r.getPreferredTime() != null && !r.getPreferredTime().isBlank() ? r.getPreferredTime() : "—"
        );
    }
}

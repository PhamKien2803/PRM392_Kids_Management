package com.example.se1829_prm392_kindergartenms.Service;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    private static final String TAG = "EmailService";


    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "587";
    private static final String SENDER_EMAIL = "kidora.kindergartenms@gmail.com";
    private static final String SENDER_PASSWORD = "xwdr mzbm xcaq wklf";

    public interface EmailCallback {
        void onSuccess(String newPassword);
        void onFailure(String error);
    }

    public static void sendNewPasswordEmail(String recipientEmail, EmailCallback callback) {
        new SendEmailTask(recipientEmail, callback).execute();
    }

    private static class SendEmailTask extends AsyncTask<Void, Void, String> {
        private String recipientEmail;
        private EmailCallback callback;
        private String newPassword;
        private String errorMessage;

        public SendEmailTask(String recipientEmail, EmailCallback callback) {
            this.recipientEmail = recipientEmail;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                newPassword = generateRandomPassword();

                Properties props = new Properties();
                props.put("mail.smtp.host", EMAIL_HOST);
                props.put("mail.smtp.port", EMAIL_PORT);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                    }
                });

                // Tạo message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(SENDER_EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("🌟 KIDORA - Mật khẩu mới của bạn");

                String emailContent = createEmailContent(newPassword);
                message.setContent(emailContent, "text/html; charset=utf-8");

                Transport.send(message);

                Log.d(TAG, "Email sent successfully to: " + recipientEmail);
                return "SUCCESS";

            } catch (MessagingException e) {
                Log.e(TAG, "Error sending email", e);
                errorMessage = "Lỗi gửi email: " + e.getMessage();
                return "FAILURE";
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error", e);
                errorMessage = "Lỗi không xác định: " + e.getMessage();
                return "FAILURE";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if ("SUCCESS".equals(result)) {
                callback.onSuccess(newPassword);
            } else {
                callback.onFailure(errorMessage);
            }
        }
    }

    private static String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

    private static String createEmailContent(String newPassword) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }" +
                ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
                ".header { text-align: center; margin-bottom: 30px; }" +
                ".title { color: #7B1FA2; font-size: 28px; font-weight: bold; margin-bottom: 10px; }" +
                ".subtitle { color: #666; font-size: 16px; }" +
                ".password-box { background-color: #f8f9fa; border: 2px solid #7B1FA2; border-radius: 8px; padding: 20px; margin: 20px 0; text-align: center; }" +
                ".password { font-size: 24px; font-weight: bold; color: #7B1FA2; letter-spacing: 2px; }" +
                ".content { color: #333; line-height: 1.6; }" +
                ".warning { background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 5px; padding: 15px; margin: 20px 0; color: #856404; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 14px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<div class='title'>🌟 KIDORA</div>" +
                "<div class='subtitle'>Hệ thống quản lý trường học</div>" +
                "</div>" +
                "<div class='content'>" +
                "<h2 style='color: #7B1FA2;'>Mật khẩu mới của bạn</h2>" +
                "<p>Chào bạn,</p>" +
                "<p>Chúng tôi đã tạo một mật khẩu mới cho tài khoản của bạn:</p>" +
                "<div class='password-box'>" +
                "<div class='password'>" + newPassword + "</div>" +
                "</div>" +
                "<div class='warning'>" +
                "<strong>⚠️ Lưu ý quan trọng:</strong>" +
                "<ul>" +
                "<li>Vui lòng đăng nhập và đổi mật khẩu ngay lập tức</li>" +
                "<li>Không chia sẻ mật khẩu này với bất kỳ ai</li>" +
                "<li>Mật khẩu này có hiệu lực ngay lập tức</li>" +
                "</ul>" +
                "</div>" +
                "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng liên hệ với chúng tôi ngay lập tức.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Trân trọng,<br>Đội ngũ KIDORA</p>" +
                "<p><em>Email này được gửi tự động, vui lòng không trả lời.</em></p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
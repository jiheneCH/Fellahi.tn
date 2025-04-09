package tn.esprit.fallehiuser.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.Execption.EmailSendingException;

import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendActivationEmail(
            String to,
            String username,
            String confirmationUrl,
            String activationCode,
            String subject) {
        logger.info("Preparing to send activation email to: {}", to);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MULTIPART_MODE_MIXED,
                    UTF_8.name()
            );

            helper.setFrom("fallehitn@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String content = "<p>Hi " + username + ",</p>" +
                    "<p>Thank you for registering! Please activate your account by clicking the link below or using the code:</p>" +
                    "<p><a href=\"" + confirmationUrl + "\">Activate Account</a></p>" +
                    "<p>Activation Code: <b>" + activationCode + "</b></p>" +
                    "<br><p>If you did not register, please ignore this email.</p>";

            helper.setText(content, true); // true = HTML

            mailSender.send(mimeMessage);
            logger.info("Activation email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send activation email to {}: {}", to, e.getMessage());
            throw new EmailSendingException("Failed to send activation email to " + to, e);
        }
    }

    public void send(String to, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MULTIPART_MODE_MIXED,
                    UTF_8.name()
            );

            helper.setFrom("fallehitn@gmail.com");
            helper.setTo(to);
            helper.setSubject("Password Reset Request");
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(mimeMessage);
            logger.info("Password reset email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send password reset email to {}: {}", to, e.getMessage());
            throw new EmailSendingException("Failed to send password reset email to " + to, e);
        }
    }

    public String buildEmail(String title, String link) {
        return "<h3>" + title + "</h3>" +
                "<p>Click the link below to reset your password:</p>" +
                "<a href=\"" + link + "\">Reset Password</a>" +
                "<p>If you did not request this, please ignore this email.</p>";
    }
}

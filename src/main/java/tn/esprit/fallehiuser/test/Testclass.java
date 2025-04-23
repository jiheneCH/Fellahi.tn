package tn.esprit.fallehiuser.test;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Testclass {

        private final JavaMailSender mailSender;

        @PostConstruct
        public void testEmailSending() {
           /* try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo("your-email@example.com"); // replace with your real email
                message.setSubject("✅ Brevo SMTP Test");
                message.setText("This is a test email from Spring Boot using Brevo SMTP.");
                message.setFrom("fallehitn@gmail.com"); // Must be the verified sender

                mailSender.send(message);
                System.out.println("✅ Email sent successfully!");
            } catch (Exception e) {
                System.err.println("❌ Failed to send email: " + e.getMessage());
                e.printStackTrace();
            }*/
        }
    }

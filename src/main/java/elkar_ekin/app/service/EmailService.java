package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            // Set the email subject
            helper.setSubject(subject);
            // Set the email content as HTML (true indicates HTML content)
            helper.setText(content, true);
            // Send the email using JavaMailSender
            mailSender.send(message);
        } catch (MessagingException e) {
            // Log the exception (printing to stderr for simplicity, consider using a logging framework)
            System.err.println("Failed to send email: " + e.getMessage());
            throw e;
        }
    }
}

package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elkar_ekin.app.model.PasswordResetToken;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.PasswordResetTokenRepository;
import elkar_ekin.app.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepository.save(myToken);
    }

    @Override
    public void sendPasswordResetEmail(String username) throws MessagingException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("No user found with username: " + username);
        }

        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);

        String resetUrl = "http://localhost:8080/resetPassword?token=" + token;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(user.getEmail());
        helper.setSubject("Password Reset Request");

        String content = "<html><body>" +
                "<img src='cid:headerImage' style='width:100%;'/><br>" +
                "<p>Click the link below to reset your password:</p>" +
                "<p><a href=\"" + resetUrl + "\">Reset Password</a></p>" +
                "<img src='cid:footerImage' style='width:100%;'/><br>" +
                "</body></html>";

        helper.setText(content, true);

        ClassPathResource headerImage = new ClassPathResource("static/images/icono.png");
        ClassPathResource footerImage = new ClassPathResource("static/images/icono.png");

        helper.addInline("headerImage", headerImage);
        helper.addInline("footerImage", footerImage);

        mailSender.send(mimeMessage);
    }

    @Override
    public PasswordResetToken validatePasswordResetToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken == null || passToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
        return passToken;
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passToken = validatePasswordResetToken(token);
        User user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(passToken);
    }
}

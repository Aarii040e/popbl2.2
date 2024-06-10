package elkar_ekin.app.service;

import elkar_ekin.app.model.PasswordResetToken;
import elkar_ekin.app.model.User;

import javax.mail.MessagingException;

public interface PasswordResetService {
    
    void createPasswordResetTokenForUser(User user, String token);

    void sendPasswordResetEmail(String username) throws MessagingException;

    PasswordResetToken validatePasswordResetToken(String token);

    void resetPassword(String token, String newPassword);
}

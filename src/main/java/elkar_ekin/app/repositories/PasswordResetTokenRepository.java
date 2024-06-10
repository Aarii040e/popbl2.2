package elkar_ekin.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import elkar_ekin.app.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
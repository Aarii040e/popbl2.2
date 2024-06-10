package elkar_ekin.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import elkar_ekin.app.service.PasswordResetService;

import javax.mail.MessagingException;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    // Display the forgot password form
    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("currentPage", "forgotPassword");
        System.out.println("En password recovery");
        return "forgotPassword";
    }

    // Process submission of the forgot password form
    @PostMapping("/forgotPassword")
    public String processForgotPasswordForm(@RequestParam String username, Model model) {
        try {
            // Send password reset email
            passwordResetService.sendPasswordResetEmail(username); 
            model.addAttribute("message", "A password reset link has been sent to the email associated with the username " + username);
        } catch (IllegalArgumentException | MessagingException e) {
            // Handle errors
            model.addAttribute("error", e.getMessage());
        }
            return "forgotPassword";
    }

    // Display the reset password form
    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        try {
            // Validate password reset token
            passwordResetService.validatePasswordResetToken(token);
            model.addAttribute("token", token);
        } catch (IllegalArgumentException e) {
            // Handle errors
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "resetPassword";
    }

    // Process submission of the reset password form
    @PostMapping("/resetPassword")
    public String processResetPasswordForm(@RequestParam String token, @RequestParam String newPassword, Model model) {
        try {
            // Reset password
            passwordResetService.resetPassword(token, newPassword);
            model.addAttribute("message", "Password has been reset successfully");
            return "redirect:/login"; // Redirect to login page after successful password reset
        } catch (IllegalArgumentException e) {
            // Handle errors
            model.addAttribute("error", e.getMessage());
        }
        return "resetPassword";
    }
}

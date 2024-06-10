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

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("currentPage", "forgotPassword");
        System.out.println("En password recovery");
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPasswordForm(@RequestParam String username, Model model) {
        try {
            passwordResetService.sendPasswordResetEmail(username);
            model.addAttribute("message", "A password reset link has been sent to the email associated with the username " + username);
        } catch (IllegalArgumentException | MessagingException e) {
            model.addAttribute("error", e.getMessage());
        }
            return "forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        try {
            passwordResetService.validatePasswordResetToken(token);
            model.addAttribute("token", token);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String processResetPasswordForm(@RequestParam String token, @RequestParam String newPassword, Model model) {
        try {
            passwordResetService.resetPassword(token, newPassword);
            model.addAttribute("message", "Password has been reset successfully");
            return "redirect:/login"; // Redirect to login page after successful password reset
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "resetPassword";
    }
}

package elkar_ekin.app.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import elkar_ekin.app.service.UserService;
import jakarta.validation.Valid;
import elkar_ekin.app.dto.UserDto;

@Controller
@RequestMapping("/registration")
@SessionAttributes("userDto")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
    }

    // Step 1: Personal Info
    @GetMapping("/step1")
    public String showStep1Form(Model model) {
        model.addAttribute("currentPage", "registration");
        return "signup/signup_step1";
    }

    @PostMapping("/step1")
    public String processStep1(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "signup/signup_step1";
        }
        return "redirect:/registration/step2";
    }

    // Step 2: Account Info
    @GetMapping("/step2")
    public String showStep2Form(Model model) {
        return "signup/signup_step2";
    }

    @PostMapping("/step2")
    public String processStep2(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "signup/signup_step2";
        }
        return "redirect:/registration/step3";
    }

    // Step 3: Contact Info
    @GetMapping("/step3")
    public String showStep3Form(Model model) {
        return "signup/signup_step3";
    }

    @PostMapping("/step3")
    public String processStep3(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "signup/signup_step3";
        }
        return "redirect:/registration/step4";
    }

    // Step 4: Contact Info
    @GetMapping("/step4")
    public String showStep4Form(Model model) {
        return "signup/signup_step4";
    }

    @PostMapping("/step4")
    public String processStep4(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "signup/signup_step4";
        }
        return "redirect:/registration/step5";
    }

    // Step 5: Additional Info
    @GetMapping("/step5")
    public String showStep5Form(Model model) {
        return "signup/signup_step5";
    }

    @PostMapping("/step5")
    public String processStep4(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("ERROREAK!!!!" + result.getAllErrors());
            
            return "signup/signup_step5";
        }
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfully!");
        return "/index";
    }
}

package elkar_ekin.app.controller;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import elkar_ekin.app.service.LocationService;
import elkar_ekin.app.service.UserService;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.dto.LocationDto;
import elkar_ekin.app.model.Location;
import elkar_ekin.app.repositories.UserRepository;

@Controller
@RequestMapping("/registration")
@SessionAttributes("userDto")
public class RegistrationController {

    private final UserService userService;
    private final LocationService locationService;
    private UserRepository userRepository;
    private Location userLocation;

    public RegistrationController(UserService userService, LocationService locationService, UserRepository userRepository) {
        this.userService = userService;
        this.locationService = locationService;
        this.userRepository=userRepository;
    }

    @ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
    }

    @ModelAttribute("locationDto")
    public LocationDto locationDto() {
        return new LocationDto();
    }

    // Step 1: Personal Information
    @GetMapping("/step1")
    public String showStep1Form(Model model) {
        model.addAttribute("currentPage", "registration");
        return "signup/signup_step1";
    }

    @PostMapping("/step1")
    public String processStep1(@ModelAttribute("userDto") UserDto userDto, Model model, BindingResult result) {
        if (hasErrorsPage1(userDto, model)) {
            return "signup/signup_step1";
        }
        return "redirect:/registration/step2";
    }

    //Error detection
    public boolean hasErrorsPage1(UserDto userDto, Model model){
        if(userDto.getTelephone().length()!=9){
            model.addAttribute("error", "error.wrongTelephone");
            return true;
        }
        return false;
    }


    // Step 2: Location Info
    @GetMapping("/step2")
    public String showStep2Form(Model model) {
        return "signup/signup_step2";
    }

    @PostMapping("/step2")
    public String processStep2(@ModelAttribute("locationDto") LocationDto locationDto, Model model, BindingResult result) {
        if (hasErrorsPage2(locationDto,model)) {
            return "signup/signup_step2";
        }
        userLocation = locationService.saveLocation(locationDto);
        return "redirect:/registration/step3";
    }

    //Error detection
    public boolean hasErrorsPage2(LocationDto locationDto, Model model){
        if(locationDto.getPostCode().toString().length()!=5){
            model.addAttribute("error", "error.wrongPostCode");
            return true;
        }
        return false;
    }

    // Step 3: Contact Info
    @GetMapping("/step3")
    public String showStep3Form(Model model) {
        return "signup/signup_step3";
    }

    @PostMapping("/step3")
    public String processStep3(@ModelAttribute("userDto") UserDto userDto,Model model, BindingResult result) {
        if (hasErrorsPage3(userDto,model)) {
            return "signup/signup_step3";
        }
        return "redirect:/registration/step4";
    }

    //Error detection
    public boolean hasErrorsPage3(UserDto userDto, Model model){
        LocalDate currentDate = LocalDate.now();
        LocalDate date18YearsAgo = currentDate.minusYears(18);
        LocalDate birthLocalDate = userDto.getBirthDate().toLocalDate();
        LocalDate date100YearsAgo = currentDate.minusYears(100);
        if(birthLocalDate.isAfter(date18YearsAgo)){
            model.addAttribute("error", "error.notOldEnough");
            return true;
        }
        else if(birthLocalDate.isBefore(date100YearsAgo)){
            model.addAttribute("error", "error.userTooOld");
            return true;
        }
        return false;
    }


    // Step 2: Account Info
    @GetMapping("/step4")
    public String showStep4Form(Model model) {
        return "signup/signup_step4";
    }

    @PostMapping("/step4")
    public String processStep4(@RequestParam("password") String password, @RequestParam("password_confirmation") String passwordConfirmation,
    @ModelAttribute("userDto") UserDto userDto, Model model, BindingResult result) {
        MultipartFile image = userDto.getImageFile();
        try {
            String uploadDir = "public/img/";
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        
        if (hasErrorsPage4(userDto,password,passwordConfirmation, model)) {
            return "signup/signup_step4";
        }
        return "redirect:/registration/step5";
    }
    //Error detection
    public boolean hasErrorsPage4(UserDto userDto, String p1, String p2, Model model){
        if (userService.usernameExists(userDto.getUsername())) {
            model.addAttribute("error", "error.userAlreayExists");
            return true;
        }
        if(!p1.equals(p2)){
            model.addAttribute("error", "error.wrongPasswordConfirmation");
            return true;
        }
        return false;
    }
    

    // Step 5: Profile Info
    @GetMapping("/step5")
    public String showStep5Form(Model model) {
        return "signup/signup_step5";
    }

    public String roleCheckboxes(
        @RequestParam(name = "checkbox_client", required = false) String checkbox1,
        @RequestParam(name = "checkbox_volunteer", required = false) String checkbox2,
        Model model) {

        boolean isCheckbox_clientChecked = checkbox1 != null;
        boolean isCheckbox_volunteerChecked = checkbox2 != null;
        String emaitza = new String();
        if(isCheckbox_clientChecked && isCheckbox_volunteerChecked){
            emaitza="CV";
        }
        else if(isCheckbox_clientChecked){
            emaitza="C";
        }
        else if(isCheckbox_volunteerChecked){
            emaitza="V";
        }
        return emaitza;
    }
    
    @PostMapping("/step5")
    public String processStep5(@ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model,
        @RequestParam(name = "checkbox_client", required = false) String checkbox1,
        @RequestParam(name = "checkbox_volunteer", required = false) String checkbox2) {
        
        userDto.setRole(roleCheckboxes(checkbox1, checkbox2, model));
        userDto.setLocation(userLocation);
        
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfully!");
        return "redirect:/logi2n";
    }
    
}

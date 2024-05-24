package elkar_ekin.app.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/volunteer-view")
public class VolunteerController {

	@Autowired
	private UserRepository repository;

	@Autowired
	UserDetailsService userDetailsService;

	private final UserService userService;

	public VolunteerController (UserService userService) {
        this.userService = userService;
    }

	
	// @GetMapping({"/logout"})
	// public String logout(Model model) {
	// 	model.addAttribute("currentPage", "index");
	// 	return "index";
	// }

	@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		// UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		// model.addAttribute("role", "client");
		String username=principal.getName();
		User user = repository.findByUsername(username);
		model.addAttribute("user", user);
		// model.addAttribute("currentPage", "index");
	}

	@GetMapping("/index")
	public String volunteerIndex (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		// model.addAttribute("role", "client");
		model.addAttribute("user", userDetails);
		model.addAttribute("currentPage", "index");
		return "index";
	}

	@GetMapping("/user")
	public String clientUser (Model model) {
		model.addAttribute("currentPage", "user");
		return "user";
	}

	@PostMapping("/user/update")
    public String clientUpdateUser (@ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model, Principal principal) {
		if (result.hasErrors()) {
            return "user";
        }
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        userService.update(userDto, userDetails);
        model.addAttribute("message", "Updated Successfully!");
        return "user";
    }
	
}

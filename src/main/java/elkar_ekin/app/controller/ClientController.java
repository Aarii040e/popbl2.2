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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.LocationService;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/client-view")
@SessionAttributes("userDto")
public class ClientController {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private UserRepository repository;

	private final UserService userService;

	public ClientController (UserService userService) {
        this.userService = userService;
    }

	@ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
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
	public String clientIndex (Model model, Principal principal) {
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

	// @PostMapping("/user/update")
    // public String clientUpdateUser (Model model, Principal principal, @ModelAttribute("userDto") UserDto userDto) {
	// 	UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    //     userService.update(userDetails);
    //     model.addAttribute("message", "Updated Successfully!");
    //     return "user";
    // }

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

    // @GetMapping("/index")
    // public String getIndexPage(){
    //     return "index";
    // }

	
}

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

import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.UserRepository;

@Controller
@RequestMapping("/client-view")
public class ClientController {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private UserRepository repository;
	
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

    // @GetMapping("/index")
    // public String getIndexPage(){
    //     return "index";
    // }

	
}

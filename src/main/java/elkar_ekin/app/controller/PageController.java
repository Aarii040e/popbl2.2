package elkar_ekin.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.service.NewsItemService;


@Controller
public class PageController {

	@Autowired
	NewsItemService newsItemService;

	@Autowired
	private UserRepository repository;

		@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		if (principal != null) {
			String username=principal.getName();
			user = repository.findByUsername(username);
			model.addAttribute("user", user);
		}
	}
		
	@GetMapping({"/", "/index", "/index.html"})
	public String getIndexPage(Model model) {
		List<NewsItem> allNewsItems = newsItemService.getLastFiveNewsItems();	// Get the last five news items
		if (allNewsItems == null) {
			model.addAttribute("message", "No hay noticias disponibles.");
		} else {
			model.addAttribute("newsItemList", allNewsItems);
		}
		model.addAttribute("currentPage", "index");
		
		return "index";
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("currentPage", "login");
		model.addAttribute("message", "Login Successfully!");
		return "login";
	}

	@GetMapping({"/volunteerList"})
	public String getVolunteerList(Model model) {
		model.addAttribute("currentPage", "volunteerList");
		return "volunteerList";
	}

	@GetMapping({"/tos"})
	public String getTosPage(Model model) {
		model.addAttribute("currentPage", "tos");
		return "tos";
	}

	@GetMapping({"/error"})
	public String getErrorPage(HttpServletRequest request, Model model) {	// In case of error get the error page
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		model.addAttribute("currentPage", "error");	
		if (status != null) {	
            int statusCode = Integer.parseInt(status.toString());	
            model.addAttribute("statusCode", statusCode);		
		}
		if(user != null) {
			if(user.getRole().equals("C")){
				return "client/error";
			}
			else if(user.getRole().equals("V")){
				return "volunteer/error";
			}
			else if(user.getRole().equals("A")){
				return "admin/error";
			}
		}
		return "admin/error";
	}
}

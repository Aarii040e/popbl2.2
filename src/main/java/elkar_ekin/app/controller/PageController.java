package elkar_ekin.app.controller;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.User;
import elkar_ekin.app.service.NewsItemService;


@Controller
public class PageController {

	@Autowired
	NewsItemService newsItemService;
		
	@GetMapping({"/", "/index", "/index.html"})
	public String getIndexPage(Model model) {
		List<NewsItem> allNewsItems = newsItemService.getLastFiveNewsItems();
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
	public String getErrorPage(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		model.addAttribute("currentPage", "error");
		if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);
		}
		return "error";
	}
}

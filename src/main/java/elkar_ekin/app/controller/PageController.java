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

    @GetMapping("/admin-view")
    public String adminView() {
        return "layout_admin"; // Nombre de la vista para administradores
    }

    @GetMapping("/volunteer-view")
    public String volunteerView() {
        return "layout_volunteer"; // Nombre de la vista para voluntarios
    }

    @GetMapping("/client-view")
    public String clientView() {
        return "layout_client"; // Nombre de la vista para clientes
    }

}

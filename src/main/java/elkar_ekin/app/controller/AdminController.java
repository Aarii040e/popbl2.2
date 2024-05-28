package elkar_ekin.app.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import elkar_ekin.app.dto.NewsItemDto;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.NewsItemService;

@Controller
@RequestMapping("/admin-view")
public class AdminController {

	private User user;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	UserDetailsService userDetailsService;

	@ModelAttribute("newsItemDto")
    public NewsItemDto newsItemDto() {
        return new NewsItemDto();
    }

	@Autowired
	NewsItemService newsItemService;

	@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		String username=principal.getName();
		user = repository.findByUsername(username);
		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String adminIndex (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		model.addAttribute("currentPage", "index");
		return "index";
	}

	@GetMapping({"/newsItem/", "/newsItem/list"})
	public String listNewsItem (Model model, Principal principal) {
		List<NewsItem> allNewsItems = newsItemService.getAllNewsItems();
		model.addAttribute("currentPage", "newsItemList");
		if (allNewsItems == null) {
			model.addAttribute("message", "No hay noticias disponibles.");
		} else {
			model.addAttribute("newsItemList", allNewsItems);
		}
		return "admin/newsItemList";
	}

	@GetMapping({"/newsItem/create"})
	public String showNewsForm(Model model) {
		NewsItemDto newsItemDto = new NewsItemDto();
		model.addAttribute("currentPage", "newsItemCreate");
		model.addAttribute("newsItemDto", newsItemDto);
		return "admin/newsItemForm";
	}

	@PostMapping("/createNewsItem")
	public String createNewsItem(@ModelAttribute("newsItem") NewsItemDto newsItemDto, BindingResult result) {
		if (result.hasErrors()) {
			return "/admin-view/createNewsItem";
		}
		newsItemDto.setUser(user);
		newsItemService.save(newsItemDto);
		return "admin/newsItemList";
	}

	@GetMapping(value = "/newsItem/{newsItemID}/delete")
	public String deleteNewsItem(@PathVariable("newsItemID") String newsItemID, Model model) {
		newsItemService.deleteNewsItem(Long.parseLong(newsItemID));
		return "redirect:/admin-view/newsItem/list";
	}


	@GetMapping({"/newsItem/{newsItemID}/edit"})
	public String showNewsItemForm(@PathVariable("newsItemID") Long newsItemID, Model model) {
		NewsItemDto newsItemDto = new NewsItemDto();
		if (newsItemID != null) {
			NewsItem newsItem = newsItemService.getNewsItemByID(newsItemID);
			if (newsItem != null) {
				newsItemDto.setNewsItemID(newsItemID);
				newsItemDto.setUser(newsItem.getUser());
				newsItemDto.setTitle(newsItem.getTitle());
				newsItemDto.setBody(newsItem.getBody());
			}
		}
		model.addAttribute("newsItemDto", newsItemDto);
		model.addAttribute("isEdit", newsItemID != null);
		return "admin/newsItemForm";
	}
	
	@PostMapping({"editNewsItem"})
	public String createOrUpdateNewsItem(@ModelAttribute("newsItemDto") NewsItemDto newsItemDto, BindingResult result) {
		if (result.hasErrors()) {
			return "admin/newsItemForm";
		}
		if (newsItemDto != null) {
			// Es una actualización
			newsItemService.editNewsItem(newsItemDto.getNewsItemID(), newsItemDto);
		} else {
			// Es una creación
			newsItemDto.setUser(user);
			newsItemService.save(newsItemDto);
		}
		return "redirect:/admin-view/newsItem/list";
	}

	@GetMapping({"/clients/list", "/clients/"})
	public String listClients (Model model, Principal principal) {
		model.addAttribute("currentPage", "clientList");
		List<User> userList = repository.getUsersByRole("C");
		if (userList == null) {
			model.addAttribute("message", "No hay clientes disponibles.");
		} else {
			model.addAttribute("userList", userList);
		}
		return "admin/userList";
	}

	@GetMapping(value = "/clients/{clientID}/delete")
	public String deleteClient(@PathVariable("clientID") String clientID, Model model) {
		repository.deleteById(Long.parseLong(clientID));
		return "redirect:/admin-view/clients/list";
	}
	
	@GetMapping(value = "/clients/{clientID}")
	public String viewClient(@PathVariable("clientID") String clientID, Model model) {
		User client = repository.findByUserID(Long.parseLong(clientID));
		model.addAttribute("user", client);
		return "user";
	}

	@GetMapping({"/volunteers/list", "/volunteers/"})
	public String listVolunteers (Model model, Principal principal) {
		model.addAttribute("currentPage", "volunteerList");
		List<User> userList = repository.getUsersByRole("V");
		if (userList == null) {
			model.addAttribute("message", "No hay clientes disponibles.");
		} else {
			model.addAttribute("userList", userList);
		}
		return "admin/userList";
	}

	@GetMapping("/volunteers/{volunteerID}/delete")
	public String deleteVolunteer(@PathVariable("volunteerID") String volunteerID, Model model) {
		repository.deleteById(Long.parseLong(volunteerID));
		return "redirect:/admin-view/volunteers/list";
	}
	
	@GetMapping(value = "/volunteers/{volunteerID}")
	public String viewVolunteer(@PathVariable("volunteerID") String volunteerID, Model model) {
		User volunteer = repository.findByUserID(Long.parseLong(volunteerID));
		model.addAttribute("user", volunteer);
		return "user";
	}
}

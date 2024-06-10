package elkar_ekin.app.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import elkar_ekin.app.dto.CommentDto;
import elkar_ekin.app.model.Comment;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/newsItem")
public class NewsItemController {

	@Autowired
	NewsItemService userDetailsService;

	@Autowired
	NewsItemService newsItemService;
	@Autowired
	UserService userService;

	@Autowired
	private UserRepository repository;

	private User user;

	@ModelAttribute
	public void commonUser(Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			user = repository.findByUsername(username);
			model.addAttribute("user", user);
		}
	}

	@GetMapping({"/list", "/"})
public String listNewsItem(Model model, Principal principal) {
    List<NewsItem> allNewsItems = newsItemService.getAllNewsItems();
    model.addAttribute("currentPage", "newsList");
    if (allNewsItems == null) {
        model.addAttribute("message", "No hay noticias disponibles.");
    } else {
        model.addAttribute("newsItemList", allNewsItems);
    }

    if (principal != null) {
        // Assuming you have a service to fetch user by username
        User user = userService.findByUsername(principal.getName());

        if (user != null) {
            String role = user.getRole();
            if ("C".equals(role)) {
                return "client/newsItemList";
            } else if ("V".equals(role)) {
                return "volunteer/newsItemList";
            } else if ("A".equals(role)) {
                return "admin/newsItemList";
            }
        }
    }

    // Default view if user is not logged in or role is not recognized
    return "newsItem/newsItemList";
}


	@GetMapping(value = "/{newsItemID}")
	public String deleteNewsItem(@PathVariable("newsItemID") String newsItemID, Model model) {
		NewsItem newsItem = newsItemService.getNewsItemByID(Long.parseLong(newsItemID));
		model.addAttribute("newsItem", newsItem);
		// DTO comments
		CommentDto commentDto = new CommentDto();
		model.addAttribute("commentDto", commentDto);
		// To list all the comments
		List<Comment> allComments = newsItemService.getAllCommentsByNewsItemId(newsItem);
		if (allComments == null) {
			model.addAttribute("message", "No hay comentarios.");
		} else {
			model.addAttribute("commentList", allComments);
		}

		// Check the role of the current user and determine the appropriate view to return
		if (user != null){
			if (user.getRole().equals("C")) {
				return "client/newsItem";
			} else if (user.getRole().equals("V")) {
				return "volunteer/newsItem";
			} else if (user.getRole().equals("A")) {
				return "admin/newsItem";
			}
		}
		return "newsItem/newsItem";
	}

	@PostMapping("/{newsItemID}/createComment")
	public String createComment(@PathVariable("newsItemID") String newsItemID,
			@ModelAttribute("commentDto") CommentDto commentDto, Model model) {
		NewsItem newsItem = newsItemService.getNewsItemByID(Long.parseLong(newsItemID));	// Get the news item
		commentDto.setNewsItem(newsItem);
		commentDto.setUser(user);
		newsItemService.saveComment(commentDto);	// Save the comment

		return "redirect:/newsItem/" + newsItem.getNewsItemID();	
	}

	@GetMapping("/search")
	public String searchNewsItems(@RequestParam("keyword") String keyword, Model model) {	// Search for news items
		List<NewsItem> searchResults = newsItemService.searchNewsItems(keyword);
		model.addAttribute("newsItemList", searchResults);
		if (user.getRole().equals("C")) {
			return "client/newsItemList";
		} else if (user.getRole().equals("V")) {
			return "volunteer/newsItemList";
		} else if (user.getRole().equals("A")) {
			return "admin/baseNewsItemList";
		}
		return "newsItem/newsItemList";
	}

}

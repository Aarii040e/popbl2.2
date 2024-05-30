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

@Controller
@RequestMapping("/newsItem")
public class NewsItemController {

	@Autowired
	NewsItemService userDetailsService;

	@Autowired
	NewsItemService newsItemService;

	@Autowired
	private UserRepository repository;

	private User user;

	@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		// UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		// model.addAttribute("role", "client");
		if (principal != null) {
			String username=principal.getName();
			user = repository.findByUsername(username);
			model.addAttribute("user", user);
		}
		// model.addAttribute("currentPage", "index");
	}

	@GetMapping({"/list", "/"})
	public String listNewsItem (Model model, Principal principal) {
		List<NewsItem> allNewsItems = newsItemService.getAllNewsItems();
		model.addAttribute("currentPage", "newsList");
		if (allNewsItems == null) {
			model.addAttribute("message", "No hay noticias disponibles.");
		} else {
			model.addAttribute("newsItemList", allNewsItems);
		}
		return "newsItem/newsItemList";
	}

	@GetMapping(value = "/{newsItemID}")
	public String deleteNewsItem(@PathVariable("newsItemID") String newsItemID, Model model) {
		NewsItem newsItem = newsItemService.getNewsItemByID(Long.parseLong(newsItemID));
		model.addAttribute("newsItem", newsItem);
		//To 																																																						 comments
		CommentDto commentDto = new CommentDto();
		model.addAttribute("commentDto", commentDto);
		//To list all the comments
		List<Comment> allComments = newsItemService.getAllCommentsByNewsItemId(newsItem);
		if (allComments == null) {
			model.addAttribute("message", "No hay comentarios.");
		} else {
			model.addAttribute("commentList", allComments);
		}
		return "newsItem/newsItem";
	}

	@PostMapping("/{newsItemID}/createComment")
	public String createComment(@PathVariable("newsItemID") String newsItemID,@ModelAttribute("commentDto") CommentDto commentDto, Model model) {
		NewsItem newsItem = newsItemService.getNewsItemByID(Long.parseLong(newsItemID));
		commentDto.setNewsItem(newsItem);
		commentDto.setUser(user);
		newsItemService.saveComment(commentDto);
		return "redirect:/newsItem/"+newsItem.getNewsItemID();
	}

	@GetMapping("/search")
    public String searchNewsItems(@RequestParam("keyword") String keyword, Model model) {
        List<NewsItem> searchResults = newsItemService.searchNewsItems(keyword);
        model.addAttribute("newsItemList", searchResults);
        return "newsItem/newsItemList";
    }
	
}

package elkar_ekin.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import elkar_ekin.app.dto.CategoryDto;
import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.Category;
import elkar_ekin.app.repositories.CategoryRepository;
import elkar_ekin.app.service.CategoryService;
import elkar_ekin.app.service.TaskService;



@Controller
@RequestMapping("client-view/createTask")
@SessionAttributes("taskDto")
public class TaskController {

    @Autowired
    CategoryRepository catRepository;

    private final CategoryService catService;
    private final TaskService taskService;
    private Category taskCategory;

    public TaskController(TaskService taskService, CategoryService catService) {
        this.taskService=taskService;
        this.catService=catService;
    }

    @ModelAttribute("taskDto")
    public TaskDto taskDto() {
        return new TaskDto();
    }

    @ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
    }

    @ModelAttribute("categoryDto")
    public CategoryDto categoryDto() {
        return new CategoryDto();
    }

    @GetMapping("/page1")
    public String showStep1Form(Model model) {
        model.addAttribute("currentPage", "createTask");
        return "client-view/createTask/page1";
    }

    public Category roleCheckboxes(
        @RequestParam(value = "radio_cleaning", required = false) String radioCleaning,
        @RequestParam(value = "radio_reforestation", required = false) String radioReforestation,
        @RequestParam(value = "radio_tutoring", required = false) String radioTutoring,
        @RequestParam(value = "radio_sportsevent", required = false) String radioSportsEvent,
        @RequestParam(value = "radio_simple_purchase", required = false) String radioSimplePurchase,
        @RequestParam(value = "radio_elaborate_purchase", required = false) String radioElaboratePurchase,
        @RequestParam(value = "radio_walk", required = false) String radioWalk,
        @RequestParam(value = "radio_hangout", required = false) String radioHang,
        @RequestParam(value = "radio_cooking", required = false) String radioCooking,
        @RequestParam(value = "radio_pet", required = false) String radioPet,
        @RequestParam(value = "radio_doctor", required = false) String radioDoctor,
        Model model) {
        
        boolean isradio_cleaningChecked = radioCleaning != null;
        boolean isradio_reforestationChecked = radioReforestation != null;
        boolean isradio_tutoringChecked = radioTutoring != null;
        boolean isradio_sportseventChecked = radioSportsEvent != null;
        boolean isradio_simple_purchaseChecked = radioSimplePurchase != null;
        boolean isradio_elaborate_purchaseChecked = radioElaboratePurchase != null;
        boolean isradio_walkChecked = radioWalk != null;
        boolean isradio_hangoutChecked = radioHang != null;
        boolean isradio_cookingChecked = radioCooking != null;
        boolean isradio_petChecked = radioPet != null;
        boolean isradio_doctorChecked = radioDoctor != null;
        Category emaitza = new Category();


        if (isradio_cleaningChecked || isradio_reforestationChecked) {
            emaitza = catRepository.findByName("Voluntariado medioambiental");
        }
        else if (isradio_tutoringChecked) {
            emaitza = catRepository.findByName("Voluntariado educativo");
        }
        else if (isradio_sportseventChecked) {
            emaitza = catRepository.findByName("Voluntariado deportivo");
        }
        else if (isradio_simple_purchaseChecked || isradio_elaborate_purchaseChecked || isradio_walkChecked
                || isradio_hangoutChecked || isradio_cookingChecked || isradio_petChecked || isradio_doctorChecked) {
            emaitza = catRepository.findByName("Voluntariado social");
        }
        return emaitza;
    }

    @PostMapping("/page1")
    public String processStep1(@ModelAttribute("categoryDto") CategoryDto categoryDto, BindingResult result) {
        if (result.hasErrors()) {
            return "client-view/createTask/page1";
        }
        return "redirect:/client-view/createTask/page2";
    }

    // Step 2: Location Info
    @GetMapping("/page2")
    public String showStep2Form(Model model) {
        return "client-view/createTask/page2";
    }
}

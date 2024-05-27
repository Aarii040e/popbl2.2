package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import elkar_ekin.app.dto.CategoryDto;
import elkar_ekin.app.model.Category;
import elkar_ekin.app.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
    
    @Autowired
	private CategoryRepository catRepository;

    public Category save(CategoryDto categoryDto) {
    // Pass data from UserDto to User
        Category category = new Category(categoryDto.getName());
        return catRepository.save(category);
	}
}

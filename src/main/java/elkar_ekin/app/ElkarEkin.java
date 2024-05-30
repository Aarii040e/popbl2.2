package elkar_ekin.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import elkar_ekin.app.model.Category;
import elkar_ekin.app.repositories.CategoryRepository;

@SpringBootApplication
public class ElkarEkin {
	public static void main(String[] args) {
		SpringApplication.run(ElkarEkin.class, args);


		Category category1 = new Category("Voluntariado ambiental");
		Category category2 = new Category("Voluntariado deportivo");
		Category category3 = new Category("Voluntariado educativo");
		Category category4 = new Category("Voluntariado social");

	}

}

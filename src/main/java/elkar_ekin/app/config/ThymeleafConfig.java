package elkar_ekin.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nz.net.ultraq.thymeleaf.LayoutDialect;


@Configuration
public class ThymeleafConfig {
    /*@Bean
    public SpringTemplateEngine  SpringTemplateEngine()
    {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new LayoutDialect());
        return engine; 
    }*/

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
}
}
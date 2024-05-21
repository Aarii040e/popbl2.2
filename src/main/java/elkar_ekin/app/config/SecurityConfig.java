package elkar_ekin.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import elkar_ekin.app.service.CustomSuccessHandler;
import elkar_ekin.app.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	@Autowired
	CustomSuccessHandler customSuccessHandler;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// //1. saiakera
	// @Bean
	// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
	// 	http.csrf(c -> c.disable())
		
	// 	.authorizeHttpRequests(request -> request
	// 			.requestMatchers("/admin-view/**").hasAuthority("A")
	// 			.requestMatchers("/volunteer-view/**").hasAuthority("V")
	// 			.requestMatchers("/client-view/**").hasAuthority("C")
	// 			.requestMatchers("/registration/**","/login", "/index", "/tos").permitAll()
	// 			.requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**")
	// 			.permitAll()
	// 			.anyRequest().authenticated())
		
	// 	.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/userLogin")
	// 			.successHandler(customSuccessHandler).permitAll())
		
	// 	.logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
	// 			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	// 			.logoutSuccessUrl("/login?logout").permitAll());
		
	// 	return http.build();
	// }
	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		http.csrf(c -> c.disable())
		
		.authorizeHttpRequests(request -> request
			.requestMatchers("/**").permitAll())
			// .requestMatchers(PathRequest.toH2Console()).permitAll()
			// .antMatchers("/", "/index").permitAll()
			// .antMatchers("/volunteer-view").hasAuthority("V")
			// .antMatchers("/client-view").hasAuthority("C")
			// .antMatchers("/registration", "/css/**").permitAll()
			// .anyRequest().authenticated())
		
		.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/userLogin")
				.successHandler(customSuccessHandler).permitAll())
		
		.logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").permitAll());
				
		return http.build();
		
	}



	@Autowired
	public void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

}

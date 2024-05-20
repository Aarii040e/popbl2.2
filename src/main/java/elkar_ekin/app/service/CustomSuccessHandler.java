package elkar_ekin.app.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		var authourities = authentication.getAuthorities();
		var roles = authourities.stream().map(r -> r.getAuthority()).findFirst();
		
		if (roles.orElse("").equals("V")) {
			//The user is a volunteer
			response.sendRedirect("/volunteer-view");
		} else if (roles.orElse("").equals("C")) {
			//The user is a client
			response.sendRedirect("/client-view");
		} else if (roles.orElse("").equals("A")) {
			//The user is a client
			response.sendRedirect("/admin-view");
		} else {
			response.sendRedirect("/error");
		}
		
	}

}

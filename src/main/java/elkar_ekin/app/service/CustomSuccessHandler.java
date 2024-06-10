package elkar_ekin.app.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = null;
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("A")) {
                redirectUrl = "/admin-view/index";
                break;
            } else if (grantedAuthority.getAuthority().equals("V")) {
                redirectUrl = "/volunteer-view/index";
                break;
            } else if (grantedAuthority.getAuthority().equals("C")) {
                redirectUrl = "/client-view/index";
                break;
            }
        }
        
        if (redirectUrl == null) {
            throw new IllegalStateException();
        }
        
        response.sendRedirect(redirectUrl);
    }
}

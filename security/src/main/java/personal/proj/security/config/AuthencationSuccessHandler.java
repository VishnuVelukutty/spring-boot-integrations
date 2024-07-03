package personal.proj.security.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthencationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
                // derived from role
                boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

                // the above "ROLE_ADMIN" is fixed and not "ADMIN" 
                if(isAdmin){
                    setDefaultTargetUrl("/admin/home");
                }else{
                    setDefaultTargetUrl("/user/home");
                }
        super.onAuthenticationSuccess(request, response, authentication);
    }

}

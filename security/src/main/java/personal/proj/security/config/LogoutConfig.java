package personal.proj.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import personal.proj.security.entity.JwtToken;
import personal.proj.security.repository.JwtTokenRepository;

@Configuration
public class LogoutConfig implements LogoutHandler {

    @Autowired
    private JwtTokenRepository jwtTokenRepository;



    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
       String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        JwtToken storedToken = jwtTokenRepository.findBytoken(token).orElse(null);

        if(storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            // storedToken.setLoggedOut(true);
            jwtTokenRepository.save(storedToken);
        }
    }
    
}

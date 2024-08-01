package security.auth.httpBasic.SecurityBasicAuth;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthenticatedFilter extends GenericFilterBean {

    AuthenticationManager authenticationManager;

    CustomAuthenticatedFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Fetching details from Header
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String username = httpRequest.getHeader("user");
        String roles = httpRequest.getHeader("roles");

        List<GrantedAuthority> authorities =  Arrays.stream(roles.split(", "))
                .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        Authentication authentication =
                new TestingAuthenticationToken(username, "", authorities);

        authenticationManager.authenticate(authentication);
        chain.doFilter(request, response);
    }
}

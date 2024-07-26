package personal.proj.security.token;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import personal.proj.security.repository.JwtTokenRepository;
import personal.proj.security.service.MyUserDetailService;

// for ensuring bearer token being passed 

@Configuration
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // check auth header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // get the bearer token
        String jwt = authHeader.substring(7);
        System.out.println("AUTH HEADER TOKEN >>> "+jwt);
        String username = jwtService.extractUsername(jwt);
        System.out.println("USername >> "+username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);

            System.out.println("Uwsername Details  >> "+userDetails.getAuthorities()+ " "+userDetails.getPassword());

            var TokenValid = jwtTokenRepository.findBytoken(jwt)
            .map(t -> !t.isExpired() && !t.isRevoked())
          .orElse(false);
          System.out.println("Token valid >>>>"+TokenValid);
          System.out.println("JWT SERVICE isTokenValid >>> >>> "+jwtService.isTokenValid(jwt,userDetails));

          // error over here jwtService.isTokenValid(jwt,userDetails) is false 
            if (jwtService.isTokenValid(jwt,userDetails) && TokenValid) {
                System.out.println("not coming >>>> >.>>> > > >");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username,
                        userDetails.getPassword(),
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                System.out.println(">>>>>>>>>>>>>. "+authenticationToken);

                System.out.println(">>>>>>>>>>>>>. "+SecurityContextHolder.getContext());
            }
        }
        filterChain.doFilter(request, response);

    }
}

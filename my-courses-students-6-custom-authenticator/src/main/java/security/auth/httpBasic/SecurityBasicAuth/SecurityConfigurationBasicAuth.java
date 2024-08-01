package security.auth.httpBasic.SecurityBasicAuth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigurationBasicAuth {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf)->
                        csrf.disable()
                )
                .cors(withDefaults())
                .sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(customPreAuthenticatedFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterBefore(customAuthenticatedFilter(), RequestHeaderAuthenticationFilter.class)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("api/hello-user").hasAnyAuthority("USER","FACULTY")
                                .requestMatchers("api/hello-faculty").hasAuthority("FACULTY")
                                .requestMatchers("/api/welcome").permitAll()
                                .requestMatchers("/api/add").permitAll()
                                .anyRequest().authenticated()
                        );

        return http.build();

    }

    @Bean
    public CustomAuthenticatedFilter customAuthenticatedFilter() throws Exception {
        CustomAuthenticatedFilter filter = new CustomAuthenticatedFilter(authenticationManager());
        System.out.println("CUSTOM- PREAUTH FILTER CALLED");
        return filter;
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        final List<AuthenticationProvider> providers = new ArrayList<>(1);
        providers.add(authAuthProvider());
        return new ProviderManager(providers);
    }

    @Bean(name = "preAuthProvider")
    AuthenticationProvider authAuthProvider() throws Exception {
        System.out.println("PREAUTH CALLED !");
        AuthenticationProvider provider = new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                System.out.println("AUTH PROVIDER CALLED FOR USER");
                return authentication;
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return true;
            }
        };

        return provider;
    }

//    @Bean
//    UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() throws Exception {
//        System.out.println("CUSTOM USER DETAILS INSTANCE CALLED !");
//        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
//
//// Providing final userDetailsServiceInstance necessary fot customPreauth to work properly
//        wrapper.setUserDetailsService(userDetailsService);
//        return wrapper;
//    }

}

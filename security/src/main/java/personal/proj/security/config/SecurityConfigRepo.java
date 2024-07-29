package personal.proj.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import personal.proj.security.service.MyUserDetailService;
import personal.proj.security.token.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigRepo {

	@Autowired
	private MyUserDetailService myDetailService;

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	// authorize i.e allow pages
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(registry -> {
					// this will allow you to register users
					registry.requestMatchers("/home", "/register/**", "/authenticate", "/login","/token/refresh").permitAll();
					registry.requestMatchers("/admin/home").hasAuthority(Role.ADMIN.name());
					registry.requestMatchers("/user/home").hasAuthority(Role.USER.name());
					// not allow anything else
					registry.anyRequest().authenticated();
				})

				/* Session creation policy needs to changed to stateless for connecting to React */
				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider()).addFilterBefore(
						jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	// linking user details with security configs
	@Bean
	public UserDetailsService userDetailsService() {
		return myDetailService;
	}

	// spring security core
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(myDetailService);
		provider.setPasswordEncoder(passworEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(authenticationProvider());
	}

	@Bean
	public PasswordEncoder passworEncoder() {
		return new BCryptPasswordEncoder();
	}

}

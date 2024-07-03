package personal.proj.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import personal.proj.security.model.MyUser;
import personal.proj.security.repo.MyUserRepository;

@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	private MyUserRepository repository;

	// this is a built in method provided by UserDetailsService
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MyUser> user = repository.findByuserName(username);
		if (user.isPresent()) {
			// convert it into in userdetail class 
			var userObj = user.get();
			return User.builder().username(userObj.getUserName()).password(userObj.getPassword())
					.roles(getRoles(userObj)).build();

		} else {

			// spring security built in
			throw new UsernameNotFoundException(username);
		}
	}

	// getting user roles 
	private String[] getRoles(MyUser user) {
		if (user.getRole() == null) {
			return new String[] { "USER" };
		}
		return user.getRole().split(",");
	}

}

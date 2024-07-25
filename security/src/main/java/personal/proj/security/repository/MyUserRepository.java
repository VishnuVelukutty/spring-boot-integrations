package personal.proj.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import personal.proj.security.entity.MyUser;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
	// to prevent null value 
	// keep same as model definition
	Optional<MyUser> findByuserName(String userName);
	

}

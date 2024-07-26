package personal.proj.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import personal.proj.security.entity.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer> {

    @Query(value = """
            select t from JwtToken t inner join MyUser u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<JwtToken> findAllTokens(Integer id);

    Optional<JwtToken> findBytoken(String token);
}

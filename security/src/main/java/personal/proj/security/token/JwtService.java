package personal.proj.security.token;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class JwtService {

    private static final String SECRET = "BB98ED41E7B872C5AB9D884380E560CDD054A49B6761D72C7EB605A00E44818504232993DD92C04BD401CB089DD1977C862AA1725D3D8E92C074F4746DB8E6A2";

    // valid for 5 minutes and after that same user need to generate new token 
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(5);
    
    private static final long REFRESH_VALIDITY = TimeUnit.MINUTES.toMillis(10);

    public String generateToken(UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("issuer", "Vishnu");
        return Jwts.builder()
                .claims(claims) // additional JWT keys values
                /*  the user for whom the token is generated */
                .subject(userDetails.getUsername()) 
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    // converting to 

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }


    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                 .verifyWith(generateKey())
                 .build()
                 .parseSignedClaims(jwt)
                 .getPayload();
    }



// to regenerate token after expiration 
    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
        .claims(claims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + REFRESH_VALIDITY))
        .signWith(generateKey() )
        .compact();

    }

    // checking if token is valid or not 
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String userName = extractUsername(jwt);
        Claims claims = getClaims(jwt);
        return userName.equals(userDetails.getUsername()) && claims.getExpiration().before(new Date());
    }
    

}

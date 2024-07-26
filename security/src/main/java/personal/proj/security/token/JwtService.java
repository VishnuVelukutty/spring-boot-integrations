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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;


    // valid for 10 minutes and after that same user need to generate new token
    // private long VALIDITY = TimeUnit.MINUTES.toMillis(jwtExpiration);
    private long VALIDITY = 36000000;

    

    // valid for a week
    // private long REFRESH_VALIDITY = TimeUnit.MINUTES.toMillis(refreshTokenExpiration);
    private long REFRESH_VALIDITY = 36000000;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("issuer", "Vishnu");
        System.out.println("Access token >>> "+VALIDITY);
        return tokenBuilder(claims, userDetails, VALIDITY);
    }

    // decoding secret key into base64
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
    public String generateRefreshToken(UserDetails userDetails) {
        System.out.println("Validity >>> >>> "+SECRET);

        System.out.println("Validity >>> >>> "+REFRESH_VALIDITY);
        return tokenBuilder(new HashMap<>(), userDetails, REFRESH_VALIDITY);
    }

    private String tokenBuilder(Map<String, Object> extraClaims,
            UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(generateKey())
                .compact();
    }

    // checking if token is valid or not
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String userName = extractUsername(jwt);
        Claims claims = getClaims(jwt);
        return userName.equals(userDetails.getUsername()) && claims.getExpiration().before(new Date());
    }

}

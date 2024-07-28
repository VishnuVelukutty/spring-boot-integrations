package personal.proj.security.service;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import personal.proj.security.entity.JwtToken;
import personal.proj.security.entity.MyUser;
import personal.proj.security.repository.JwtTokenRepository;
import personal.proj.security.repository.MyUserRepository;
import personal.proj.security.token.JwtService;
import personal.proj.security.token.TokenType;

@Service
public class ReactConnectSrv {
    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private MyUserRepository userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JSONObject registerUser(JSONObject requestJSON) {
        JSONObject responseJSON = new JSONObject();

        String username = requestJSON.getString("userName");
        String password = requestJSON.getString("userPass");
        String role = requestJSON.getString("userRole");

        try {
            MyUser myUser = new MyUser();
            myUser.setUserName(username);
            myUser.setPassword(passwordEncoder.encode(password));
            myUser.setRole(role);
            myUserRepository.save(myUser);

            String jwtToken = jwtService.generateToken(myUser);
            String refreshToken = jwtService.generateRefreshToken(myUser);

            saveUserToken(myUser, jwtToken);

            responseJSON.put("Session", "Registration");
            responseJSON.put("token", jwtToken);
            responseJSON.put("refreshtoken", refreshToken);
            responseJSON.put("status", 200);
        } catch (Exception e) {
            responseJSON.put("status", 400);
            responseJSON.put("Error", e);
        }

        return responseJSON;

    }

    public JSONObject login(JSONObject requestJSON) {

        JSONObject responseJSON = new JSONObject();

        try {

            String username = requestJSON.getString("userName");
            String password = requestJSON.getString("userPass");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            Optional<MyUser> user = userRepo.findByuserName(username);

            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            if (user.isPresent()) {
                MyUser optionalUser = user.get();

                String jwtToken = jwtService.generateToken(userDetails);
                String refreshJwtToken = jwtService.generateRefreshToken(userDetails);
                revokeAllUserTokens(optionalUser);
                saveUserToken(optionalUser, jwtToken);

                responseJSON.put("Session", "Login");
                // access token
                responseJSON.put("token", jwtToken);
                // refresh token
                responseJSON.put("refreshToken", refreshJwtToken);
                responseJSON.put("role", user.get().getRole());
                responseJSON.put("status", 200);
            } else {
                responseJSON.put("status", 401); // Unauthorized status code
                responseJSON.put("message", "Invalid username or password");
            }

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            // Handle authentication failures
            responseJSON.put("status", 401); // Unauthorized status code
            responseJSON.put("message", "Invalid username or password");
        }

        return responseJSON;

    }

    public JSONObject refreshToken(HttpServletRequest request,
            HttpServletResponse response) throws java.io.IOException {

        JSONObject responseJSON = new JSONObject();

        // same authfilter
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            responseJSON.put("status", 403);
        }

        refreshToken = authHeader.substring(7);

        userName = jwtService.extractUsername(refreshToken);

        // no need to check if the user authenticated since this is refresh token
        if (userName != null) {
            var user = userRepo.findByuserName(userName)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                // optional create a refresh token column store it can be used to revoke it

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                responseJSON.put("token", accessToken);
                responseJSON.put("refreshToken", refreshToken);
                responseJSON.put("status", 200);

            }
        }

        return responseJSON;

    }

    /* functions */
    private void saveUserToken(MyUser myUser, String jwtToken2) {
        JwtToken saveToken = new JwtToken();
        saveToken.setToken(jwtToken2);
        saveToken.setTokenType(TokenType.BEARER);
        saveToken.setExpired(false);
        saveToken.setRevoked(false);
        saveToken.setUser(myUser);
        jwtTokenRepository.save(saveToken);
    }

    private void revokeAllUserTokens(MyUser user) {
        var validUserTokens = jwtTokenRepository.findAllTokens(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }

}

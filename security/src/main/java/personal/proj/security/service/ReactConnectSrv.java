package personal.proj.security.service;

import java.util.HashMap;
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
import personal.proj.security.entity.MyUser;
import personal.proj.security.repository.MyUserRepository;
import personal.proj.security.token.JwtService;

@Service
public class ReactConnectSrv {
    @Autowired
    private MyUserRepository myUserRepository;

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

    private String jwtToken;
    private String username;

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

            username = requestJSON.getString("userName");
            String password = requestJSON.getString("password");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            Optional<MyUser> user = userRepo.findByuserName(username);

            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            if (user.isPresent()) {

                jwtToken = jwtService.generateToken(userDetails);
                String refreshJwtToken = jwtService.generateRefreshToken(new HashMap<>(), userDetails);

                responseJSON.put("status", 200);
                responseJSON.put("token", jwtToken);
                responseJSON.put("refreshToken", refreshJwtToken);
                responseJSON.put("role", user.get().getRole());
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

    /* public JSONObject refreshToken(JSONObject requestJSON) {
        JSONObject responseJSON = new JSONObject();
        try {
            // String username = requestJSON.getString("userName");
            // String jwtToken = requestJSON.getString("jwtToken");

            // this is just a workaround copied from login
            // proper implementation to be done

            Optional<MyUser> user = userRepo.findByuserName(username);
            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);

            // is token valid will check if the token is valid
            if (user.isPresent() && jwtService.isTokenValid(jwtToken)) {
                String refreshJwtToken = jwtService.generateToken(userDetails);
                responseJSON.put("refreshToken", refreshJwtToken);
                responseJSON.put("msg", "Token refreshed");

            } else {
                responseJSON.put("status", 401); // Unauthorized status code
                responseJSON.put("message", "Invalid username");
            }

        } catch (Exception e) {
            responseJSON.put("status", 400); // Unauthorized status code
            responseJSON.put("message", e);
        }

        return responseJSON;

    } */

    public JSONObject refreshToken(HttpServletRequest request,
            HttpServletResponse response) throws java.io.IOException {

        JSONObject responseJSON = new JSONObject();
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            responseJSON.put("status", 403); 
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userRepo.findByuserName(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accesToken = jwtService.generateToken(user);
               /*  var authResponse = AuthenticationResponse.builder()
                        .accessToken(accesToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse); */
responseJSON.put("token",accesToken);
responseJSON.put("refreshToken", refreshToken);
responseJSON.put("status", 200); 
  
            }
        }

        return responseJSON;

    }

}

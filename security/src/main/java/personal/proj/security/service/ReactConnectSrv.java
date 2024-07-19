package personal.proj.security.service;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import personal.proj.security.model.MyUser;
import personal.proj.security.repo.MyUserRepository;
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

            String username = requestJSON.getString("userName");
            String password = requestJSON.getString("password");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            Optional<MyUser> user = userRepo.findByuserName(username);

            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            if (user.isPresent()) {

                String jwtToken = jwtService.generateToken(userDetails);

                responseJSON.put("status", 200);
                responseJSON.put("token", jwtToken);
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

    public JSONObject refreshToken(JSONObject requestJSON) {
        JSONObject responseJSON = new JSONObject();
        // create a button in frontend to initate refresh token ??
        try {
            String username = requestJSON.getString("userName");
            String jwtToken = requestJSON.getString("jwtToken");

            // this is just a workaround copied from login
            // proper implementation to be done

            Optional<MyUser> user = userRepo.findByuserName(username);
            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);

            // is token valid will check if the token is valid
            if (user.isPresent() && jwtService.isTokenValid(jwtToken)) {
                String refreshJwtToken = jwtService.generateToken(userDetails);
                responseJSON.put("token", refreshJwtToken);
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

    }

}

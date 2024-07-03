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

import personal.proj.security.config.SecurityConfigRepo;
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

    public JSONObject login(JSONObject requestJSON) {

        JSONObject responseJSON = new JSONObject();

        try {

            String username = requestJSON.getString("userName");
            String password = requestJSON.getString("password");

            System.out.println("UserName :: " + username);

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

}
package personal.proj.security.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import personal.proj.security.service.ReactConnectSrv;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ReactConnect {

    @Autowired
    private ReactConnectSrv reactConnectSrv;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody String requestData) {
        JSONObject requestJSON = new JSONObject(requestData);
        JSONObject responseJSON = reactConnectSrv.login(requestJSON);

        int status = responseJSON.getInt("status");

        if (status == 200) {
            System.out.println("Success Response sent >>> " + responseJSON.toString());
            return new ResponseEntity<>(responseJSON.toString(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(responseJSON.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> register(@RequestBody String requestData) {
        JSONObject requestJSON = new JSONObject(requestData);

        System.out.println("requestData" + requestData);
        JSONObject responseJSON = reactConnectSrv.registerUser(requestJSON);

        int status = responseJSON.getInt("status");

        if (status == 200) {
            return new ResponseEntity<>(responseJSON.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseJSON.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
            HttpServletResponse response) throws java.io.IOException {
        JSONObject responseJSON = reactConnectSrv.refreshToken(request, response);

        int status = responseJSON.getInt("status");

        if (status == 200) {
            System.out.println("refreshJwtToken" + "\n" + responseJSON.toString());

            return new ResponseEntity<>(responseJSON.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseJSON.toString(), HttpStatus.BAD_REQUEST);
        }

    }

}

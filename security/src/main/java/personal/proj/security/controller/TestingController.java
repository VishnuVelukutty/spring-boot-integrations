package personal.proj.security.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestingController {

    @GetMapping("/admin/home")
    public ResponseEntity<?> adminHome() {
        JSONObject responseJson = new JSONObject();
        responseJson.put("Status", "Admin Access OK");
        return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
    }

    @GetMapping("/user/home")
    public ResponseEntity<?> userHome() {
        JSONObject responseJson = new JSONObject();
        responseJson.put("Status", "User Access OK");
        return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
    }

}

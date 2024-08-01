package security.auth.httpBasic.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//import security.auth.httpBasic.Service.UserInfoService;

@RestController
public class HelloWorld {

    @GetMapping("/api/hello-user")
//    @PreAuthorize("hasRole('USER')")
    public String helloUser(){
        return "<h1>Hello User</h1>";
    }

    @GetMapping("/api/hello-faculty")
//    @PreAuthorize("hasRole('FACULTY')")
    public String helloFac(){
        return "<h1>Hello Sir/Mam</h1>";
    }

//    @PostMapping("api/add")
//    public String addNewUser(@RequestBody UserInfo userInfo){
//        return  service.addUser(userInfo);
//    }

    @GetMapping("/api/welcome")
    public String hello(){
        return "<h1>Welcome</h1>";
    }
}

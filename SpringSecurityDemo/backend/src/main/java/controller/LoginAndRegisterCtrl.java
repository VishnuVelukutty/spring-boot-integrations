package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.LoginAndRegisterService;

@Controller
@RestController
@RequestMapping("/api")
public class LoginAndRegisterCtrl {

	
	@Autowired
	private LoginAndRegisterService loginAndRegisterService;
	
	@PostMapping("/login")
	public String loginCtrl(@RequestBody String entity) {
		//TODO: process POST request
		
		return entity;
	}
	
	@GetMapping("/register")
	public String registerCtrl(@RequestBody String entity) {
		//TODO: process POST request
		
		return entity;
	}
	
}

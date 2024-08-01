package com.Two.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Two.demo.Entity.Course;
import com.Two.demo.Repository.RepoCourses;
import com.Two.demo.Service.CURDService;

@RestController
public class Controller {
	
//	@Value("${spring.datasource.username: default username}")
//	String user;
//	
//	@Value("${spring.datasource.password: default password}")
//	String password;
//	
//	@Value("${spring.datasource.url: default url}")
//	String url;
//	
	String user="osxmxm", password="54sdcsd", url="sdc";
	@GetMapping("/user")
	
	public String dbcred() {
		return user+" "+password +" "+url;
	}
	

	@GetMapping("/courses/api/v1/courses/hello")
	public String hello() {
		return "Hello World";
	}
	
	@Autowired
	CURDService service;
	
	@GetMapping("/courses/api/v1/courses")
	public List<Course> get_all() {
		return service.getAllCourses();
	}
	
	@PostMapping("/courses/courses/api/v1/courses/add")
	public Course write_user(@RequestBody Course course) {
		return service.addAllCourses(course);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.Two.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Two.demo.Entity.Course;
import com.Two.demo.Repository.RepoCourses;

@Service
public class CURDService {
	
	@Autowired
	RepoCourses repoCourses;

	public List<Course> getAllCourses() {
		// TODO Auto-generated method stub
		return repoCourses.findAll();
	}

	public Course addAllCourses(Course course) {
		// TODO Auto-generated method stub
		return repoCourses.save(course);
	}
	

	
	
}

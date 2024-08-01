package com.Two.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Two.demo.Entity.Course;

@Repository
public interface RepoCourses extends JpaRepository<Course, Long>{

}

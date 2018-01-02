package org.cubias;

import java.util.HashMap;

import org.cubias.entities.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class McBootApplication {

	public static HashMap<Long, Student> hmapStudent;

	public static void main(String[] args) {
		hmapStudent = new HashMap<Long, Student>();
		Student s1 = new Student("Miguel", "Math");
		hmapStudent.put(s1.getId(), s1);
		
		SpringApplication.run(McBootApplication.class, args);
		
		Student s2 = new Student("Cubias", "History");
		hmapStudent.put(s2.getId(), s2);
	}
}

package org.cubias;

import java.util.HashMap;

import org.cubias.entities.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class McBootApplication {

	public static HashMap<Long, Student> hmapStudent;

	public static void main(String[] args) {

		SpringApplication.run(McBootApplication.class, args);

	}
}

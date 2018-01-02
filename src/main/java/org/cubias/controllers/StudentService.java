package org.cubias.controllers;

import java.util.HashMap;

import org.cubias.McBootApplication;
import org.cubias.entities.Student;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/student/")
public class StudentService {

	@RequestMapping(value = "all", method = RequestMethod.GET)
	public HashMap<Long, Student> getAll() {
		return McBootApplication.hmapStudent;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public Student doSave(@RequestParam(value = "name") String name,
			@RequestParam(value = "subject", defaultValue = "Unknown") String subject) {
		Student student = new Student(name, subject);
		McBootApplication.hmapStudent.put(student.getId(), student);
		return student;
	}

	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public Student doUpdate(@RequestBody Student student) throws Exception {
		if (McBootApplication.hmapStudent.containsKey(student.getId())) {
			McBootApplication.hmapStudent.put(student.getId(), student);
		} else {
			throw new Exception("Student " + student.getId() + " does not exists on our records");
		}
		return student;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	public Student doRemove(@PathVariable long id) throws Exception {
		Student student;
		if (McBootApplication.hmapStudent.containsKey(id)) {
			student = McBootApplication.hmapStudent.get(id);
			McBootApplication.hmapStudent.remove(id);
		} else {
			throw new Exception("Student " + id + " does not exists on our records");
		}
		return student;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public Student getStudent(@PathVariable long id) {
		return McBootApplication.hmapStudent.get(id);
	}
}

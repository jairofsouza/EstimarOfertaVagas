package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class StudentsHistory {

	private HashMap<String, Student> _students = new HashMap<String, Student>();
	
	public StudentsHistory() { }
	
	public void add(String id, String course, CourseStatus status) {
		
		Student st = this._students.get(id);
		
		if (st == null) {
			st = new Student(id);
			this._students.put(id, st);
		}
		
		st.addCourse(course, status);
	}

	public HashMap<String, Student> getStudents() {
		return this._students;
	}
	
	@Override
	public String toString() {
		
		String output = "";
		for (Student student : this._students.values()) {
			output += student.toString() + "\n";
		}
		
		return output;
	}
	
}

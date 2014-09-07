package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.HashMap;

public class StudentsHistory {

	private HashMap<String, Student> _students = new HashMap<String, Student>();
	
	public StudentsHistory() { }
	
	public void add(String id, String course, CourseStatus status ) {
		
		Student st = this._students.get(id);
		
		if (st == null) {
			st = new Student(id);
			this._students.put(id, st);
		}
		
		st.addCourse(course, status);
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

package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.HashMap;
import java.util.TreeSet;

public class Student {

	
	private HashMap<CourseStatus, TreeSet<Course>> courses = new HashMap<CourseStatus, TreeSet<Course>>();
	private String _id;
	
	public Student(String id) {
		this._id = id;
	}
	
	
	public void addCourse(String course, CourseStatus status) {
		
		// A linha abaixo  necess‡ria por conta das equivalncias de disciplinas
		Course course2 = CourseFactory.getCourse(course);
		
		if (!this.courses.containsKey(status))
			this.courses.put(status, new TreeSet<Course>());
		
		this.courses.get(status).add(course2);
	}
	
	public String getId() { return this._id; }
	
	@Override
	public String toString() {
		String output = "Student " + this._id;

		for (CourseStatus status : this.courses.keySet()) {
			output += ", "+status.name()+"=";
			for (Object string : this.courses.get(status).toArray()) 
				output += "," + string;
		}
		return output;
	}
	
	public TreeSet<Course> getCourses(CourseStatus cs) {
		if (!this.courses.containsKey(cs))
			this.courses.put(cs, new TreeSet<Course>());

		return courses.get(cs);
	}
	
}

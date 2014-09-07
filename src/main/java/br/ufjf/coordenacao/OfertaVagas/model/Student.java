package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.TreeSet;

public class Student {

	private TreeSet<String> __enrolledCourses = new TreeSet<String>();
	private TreeSet<String> __approvedCourses = new TreeSet<String>();
	private String _id;
	
	public Student(String id) {
		this._id = id;
	}
	
	
	public void addCourse(String course, CourseStatus status) {
		if (status == CourseStatus.APPROVED)
			this.__approvedCourses.add(course);
		else 
			this.__enrolledCourses.add(course);
	}
	
	public String getId() { return this._id; }
	
	@Override
	public String toString() {
		String output = "Student " + this._id;

		output += ", APPROVED=";
		for (Object string : this.__approvedCourses.toArray()) 
			output += "," + string;
		
		output += ", ENROLLED=";
		for (Object string : this.__enrolledCourses.toArray()) 
			output += "," + string;
		
		return output;
	}
	
}

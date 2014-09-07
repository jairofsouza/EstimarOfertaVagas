package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.ArrayList;

public class Course implements Comparable<Course> {

	String id;
	ArrayList<Course> prerequisite = new ArrayList<Course>();
	
	public Course(String id) {
		this.id = id;
	}
	
	public void addPrerequisite(Course c) {
		this.prerequisite.add(c);
	}

	
	public String getId() { return id; }
	
	@Override
	public String toString() {
		String o = id;
		if (!prerequisite.isEmpty()) {
			o += " -> [pre  ";
			for (Course course : prerequisite) o += course +",";
			o = o.substring(0,o.length()-1) + "]";
		}
		return o;
	}

	@Override
	public int compareTo(Course o) {
		return o.getId().compareTo(id);
	}
	
}

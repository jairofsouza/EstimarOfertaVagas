package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.HashMap;
import java.util.TreeSet;

public class Curriculum {

	private HashMap<Integer, TreeSet<Course>> _curriculum = 
			new HashMap<Integer, TreeSet<Course>>();
	
	private TreeSet<Course> _electives = new TreeSet<Course>();
	
	public void addMandatoryCourse(int semester, Course course) {
		
		TreeSet<Course> tree = this._curriculum.get(semester);
		if (tree == null) {
			tree = new TreeSet<Course>();
			this._curriculum.put(semester, tree);
		}
		
		tree.add(course);
	}
	
	public void addElectiveCourse(Course c) {
		this._electives.add(c);
	}
	
	public String toString() {
		String out = "";
		for (Integer semester : this._curriculum.keySet()) {
			out += "semester " + semester + ":\n";
			for (Course course : this._curriculum.get(semester)) {
				out += course + "\n";
			}
		}
	
		for (Object course : this._electives.toArray()) 
			out += "[elective] " + course + "\n";
	
		return out;
	}
	
}

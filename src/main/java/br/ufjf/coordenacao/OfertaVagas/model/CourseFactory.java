package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.HashMap;

public class CourseFactory {
	
	private static CourseFactory _instance = null;
	private HashMap<String, Course> map = new HashMap<String, Course>();
	
	protected CourseFactory() {	}
	
	private static CourseFactory getInstance() { 
		if (_instance == null)	_instance = new CourseFactory(); 
		return _instance;
	}
	
	public static Course getCourse(String id) {
		
		HashMap<String, Course> cfmap = getInstance().map;
		Course c = cfmap.get(id);
		
		if (c == null) {
			c = new Course(id);
			cfmap.put(id, c);
		}
		
		return c;		
	}
	
}

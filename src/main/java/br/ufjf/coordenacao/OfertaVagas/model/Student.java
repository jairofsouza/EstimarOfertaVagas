package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.HashMap;
import java.util.TreeSet;

public class Student {

	
	private HashMap<ClassStatus, TreeSet<Class>> classes = new HashMap<ClassStatus, TreeSet<Class>>();
	private String _id;
	
	public Student(String id) {
		this._id = id;
	}
	
	
	public void addClass(String _class, ClassStatus status) {
		
		// A linha abaixo é necessária por conta das equivalências de disciplinas
		Class _class2 = ClassFactory.getClass(_class);
		
		if (!this.classes.containsKey(status))
			this.classes.put(status, new TreeSet<Class>());
		
		this.classes.get(status).add(_class2);
	}
	
	public String getId() { return this._id; }
	
	@Override
	public String toString() {
		String output = "Student " + this._id;

		for (ClassStatus status : this.classes.keySet()) {
			output += ", "+status.name()+"=";
			for (Object string : this.classes.get(status).toArray()) 
				output += "," + string;
		}
		return output;
	}
	
	public TreeSet<Class> getClasses(ClassStatus cs) {
		if (!this.classes.containsKey(cs))
			this.classes.put(cs, new TreeSet<Class>());

		return classes.get(cs);
	}
	
}

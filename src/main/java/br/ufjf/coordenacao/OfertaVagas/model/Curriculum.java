package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.HashMap;
import java.util.TreeSet;

public class Curriculum {

	private HashMap<Integer, TreeSet<Class>> _curriculum = 
			new HashMap<Integer, TreeSet<Class>>();
	
	private TreeSet<Class> _electives = new TreeSet<Class>();
	
	public void addMandatoryClass(int semester, Class _class) {
		
		TreeSet<Class> tree = this._curriculum.get(semester);
		if (tree == null) {
			tree = new TreeSet<Class>();
			this._curriculum.put(semester, tree);
		}
		
		tree.add(_class);
	}
	
	public void addElectiveClass(Class c) {
		this._electives.add(c);
	}
	
	public String toString() {
		String out = "";
		for (Integer semester : this._curriculum.keySet()) {
			out += "semester " + semester + ":\n";
			for (Class _class : this._curriculum.get(semester)) {
				out += _class + "\n";
			}
		}
	
		for (Object _class : this._electives.toArray()) 
			out += "[elective] " + _class + "\n";
	
		return out;
	}
	
	public HashMap<Integer, TreeSet<Class>> getMandatories() {
		return _curriculum;
	}
	
	public TreeSet<Class> getElectives() {
		return _electives;
	}
	
	
}

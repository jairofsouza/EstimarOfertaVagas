package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.ArrayList;

public class Class implements Comparable<Class> {

	String id;
	ArrayList<Class> prerequisite = new ArrayList<Class>();
	
	public Class(String id) {
		this.id = id;
	}
	
	public void addPrerequisite(Class c) {
		this.prerequisite.add(c);
	}

	public ArrayList<Class> getPrerequisite() {
		return this.prerequisite;
	}
	
	public String getId() { return id; }
	
	@Override
	public String toString() {
		String o = id;
		if (!prerequisite.isEmpty()) {
			o += " -> [pre  ";
			for (Class _class : prerequisite) o += _class +",";
			o = o.substring(0,o.length()-1) + "]";
		}
		return o;
	}

	@Override
	public int compareTo(Class o) {
		return o.getId().compareTo(id);
	}
	
}

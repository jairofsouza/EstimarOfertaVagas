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
	
	public Curriculum() { }
	public Curriculum(TreeSet<Class> mandatories, TreeSet<Class> electives)
	{
		this._curriculum.put(1, mandatories);
		this._electives = electives;
	}
	
	public Curriculum(HashMap<Integer, TreeSet<Class>> mandatories, TreeSet<Class> electives)
	{
		this._curriculum = mandatories;
		this._electives = electives;
	}
	
	public void addElectiveClass(Class c) {
		this._electives.add(c);
	}
	
	@Override
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
	
	public TreeSet<Class> getMandatoriesBySemester(int n)
	{
		return _curriculum.get(n);
	}
	
	public Curriculum getCurriculumForMultiples()
	{
		Curriculum c = new Curriculum();
		
		c._electives = this._electives;
		
		TreeSet<Class> mandatories = new TreeSet<Class>();
		for(TreeSet<Class> classes: this._curriculum.values())
		{
			mandatories.addAll(classes);
		}
		
		c._curriculum = new HashMap<Integer,TreeSet<Class>>();
		c._curriculum.put(1,mandatories);
		return c;
	}
}

package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.HashMap;

public class ClassFactory {
	
	private static ClassFactory _instance = null;
	private HashMap<String, Class> map = new HashMap<String, Class>();
	
	public HashMap<String, Class> getMap()
	{
		return this.map;
	}
	
	protected ClassFactory() {	}
	
	private static ClassFactory getInstance() { 
		if (_instance == null)	_instance = new ClassFactory(); 
		return _instance;
	}
	
	public static Class getClass(String course, String curriculum, String id) {
		
		//System.out.println(key + " " + c);
		
		String key = course + ";" + curriculum + ";" + id;
		
		HashMap<String, Class> cfmap = getInstance().map;
		Class c = cfmap.get(key);
		
		if (c == null) {
			c = new Class(id);
			cfmap.put(key, c);
		}
		
		//System.out.println(key + " " + c.getClass().);
		return c;		
	}
	
	public static void addClass(String course, String curriculum, String id, Class c) {
		
		id = course + ";" + curriculum + ";" + id;
		
		getInstance().map.put(id, c);
	}
	
	public static boolean contains(String course, String curriculum, String id) {
		
		id = course + ";" + curriculum + ";" + id;
		
		return getInstance().map.containsKey(id);
	}
	
	public static String makeString() {
		String out = "";
		HashMap<String, Class> m = getInstance().map;
		for (String string : m.keySet()) 
			out +=  "("+string + ", " + m.get(string) + ") \n";
		
		return out;
	}
	
}

package br.ufjf.coordenacao.OfertaVagas.loader;

import org.apache.commons.csv.CSVRecord;

public class StudentGradeFilter implements IFilter {
	
	String _grade;
	
	public StudentGradeFilter(String Class)
	{
		this._grade = Class;
	}
	
	@Override
	public boolean check(CSVRecord record) {
		return (record.get(3).trim().equals(this._grade));
	}

}
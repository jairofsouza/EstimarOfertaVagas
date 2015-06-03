package br.ufjf.coordenacao.OfertaVagas.loader;

import java.util.ArrayList;

import org.apache.commons.csv.CSVRecord;

public class CurriculumDisciplineFilter implements IFilter{

	private ArrayList<String> _disciplines;
	
	CurriculumDisciplineFilter(ArrayList<String> disciplines)
	{
		this._disciplines = disciplines; 
	}
	
	public boolean check(CSVRecord record)
	{
		return (this._disciplines.contains(record.get(1)));
	}
	
}

package br.ufjf.coordenacao.OfertaVagas.loader;

import org.apache.commons.csv.CSVRecord;

public class CurriculumElectivesFilter implements IFilter {

	public CurriculumElectivesFilter()
	{}
	
	public boolean check(CSVRecord record)
	{
		//TODO auto-generated return
		return (record.get(0).length() > 3);
	}
}

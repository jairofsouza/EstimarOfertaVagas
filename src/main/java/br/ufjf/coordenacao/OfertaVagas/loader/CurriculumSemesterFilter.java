package br.ufjf.coordenacao.OfertaVagas.loader;

import org.apache.commons.csv.CSVRecord;

public class CurriculumSemesterFilter implements IFilter {

	private String _semester;
	
	public CurriculumSemesterFilter(String semester)
	{
		this._semester = semester;
	}
	
	@Override
	public boolean check(CSVRecord record) {
		return (record.get(0).trim().equals(this._semester));
	}
}
package br.ufjf.coordenacao.OfertaVagas.loader;

import org.apache.commons.csv.CSVRecord;

public class StudentIngressFilter implements IFilter {

	private String _ingressYear;
	
	public StudentIngressFilter(String year)
	{
		this._ingressYear = year;
	}
	
	@Override
	public boolean check(CSVRecord record) {
		return this._ingressYear.equals(record.get(1).substring(0,4).trim());
	}

}

package br.ufjf.coordenacao.OfertaVagas.loader;

import org.apache.commons.csv.CSVRecord;

public interface IFilter {

	public boolean check(CSVRecord record);
	
}

package br.ufjf.coordenacao.OfertaVagas.loader;

import org.apache.commons.csv.CSVRecord;

public class NoFilter implements IFilter {

	@Override
	public boolean check(CSVRecord record) {
		return true;
	}

}

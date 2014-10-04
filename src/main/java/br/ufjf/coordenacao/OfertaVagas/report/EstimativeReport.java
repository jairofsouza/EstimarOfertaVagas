package br.ufjf.coordenacao.OfertaVagas.report;

import java.io.IOException;
import java.io.PrintStream;

import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public abstract class EstimativeReport {

	protected PrintStream out;
	
	public EstimativeReport(PrintStream out) {
		this.out = out;
	}
	
	public void generate(EstimativesResult result, StudentsHistory sh, Curriculum cur) throws IOException { }


}

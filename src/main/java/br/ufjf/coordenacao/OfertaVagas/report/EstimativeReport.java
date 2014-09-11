package br.ufjf.coordenacao.OfertaVagas.report;

import java.io.IOException;
import java.io.PrintStream;

import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;

public abstract class EstimativeReport {

	protected PrintStream out;
	
	public EstimativeReport(PrintStream out) {
		this.out = out;
	}
	
	public void abgenerate(EstimativesResult result) throws IOException { }


}

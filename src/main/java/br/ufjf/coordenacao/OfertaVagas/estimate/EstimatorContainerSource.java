package br.ufjf.coordenacao.OfertaVagas.estimate;

import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class EstimatorContainerSource {
	public StudentsHistory sh;
	public Curriculum c;
	
	public EstimatorContainerSource(StudentsHistory sh, Curriculum c)
	{
		this.sh = sh;
		this.c = c;
	}
}

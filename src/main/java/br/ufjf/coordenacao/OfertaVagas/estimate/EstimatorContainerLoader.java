package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.model.Class;

public class EstimatorContainerLoader {
	private ArrayList<EstimatorContainerSource> ecs;
	
	
	public EstimatorContainerLoader() {
		this.ecs = new ArrayList<EstimatorContainerSource>();
		
	}
	
	public void add(Curriculum c, StudentsHistory sh)
	{
		this.ecs.add(new EstimatorContainerSource(sh, c));
	}
	
	public ArrayList<EstimatorContainerSource> getSource()
	{
		return this.ecs;
	}
	
	public Curriculum getCurriculum()
	{
		Curriculum cur = new Curriculum();
		
		for(EstimatorContainerSource source: this.ecs)
		{
			HashMap<Integer, TreeSet<Class>> mandatories = source.c.getMandatories();
			TreeSet<Class> electives = source.c.getElectives();
		}
		
		return cur;
	}
}


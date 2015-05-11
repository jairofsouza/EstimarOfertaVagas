package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class EstimatorContainer2 {
	private ArrayList<EstimatorContainerSource> _ecl;
	private HashMap<String, EstimativesResult> _listER;
	private HashMap<String, Estimative> _result;
	private TreeSet<Class> _mandatories;
	private TreeSet<Class> _electives;
	private Curriculum _joinedCurriculum;
	
	private void process()
	{
		for(EstimatorContainerSource ecs: this._ecl)
		{
				
			Estimator estimator = new Estimator(ecs.c, ecs.sh);
			EstimativesResult result = estimator.populateData().process(0.9f, 0.6f, 0.7f, 0.8f, 0.5f);
			
			ArrayList<Estimative> estimatives = result.getEstimatives();
			for(Estimative e: estimatives)
			{
				String ClassId = e.getClassId();
				if(this._result.containsKey(ClassId))
				{
					Estimative existent = this._result.get(ClassId);
					
					int hasPrereq = existent.getQtdHasAllPrereq()
							+ e.getQtdHasAllPrereq();
					int qdtCanHaveAllPreq = existent.getQtdCanHaveAllPreq()
							+ e.getQtdCanHaveAllPreq();
					int isEnrolled = existent.getQtdEnrolled()
							+ e.getQtdEnrolled();
					int reprovG = existent.getQtdReprovedGrade()
							+ e.getQtdReprovedGrade();
					int reprovF = existent.getQtdReprovedFreq()
							+ e.getQtdReprovedFreq();
					
					e.updateEstimative(hasPrereq, isEnrolled, qdtCanHaveAllPreq, reprovG, reprovF);
				}
				
				this._result.put(ClassId, e);
			}
		}
	}
	
	public EstimativesResult getResult()
	{
		this.process();
		
		EstimativesResult er = new EstimativesResult();
		
		Iterator<Entry<String, Estimative>> it = this._result.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<String, Estimative> e = it.next();
			er.addEstimative(e.getValue());
			
			System.out.println(e.getKey() + " adicionado.");
		}
		
		return er;
	}
	
	public void joinCurriculum() {
		for(EstimatorContainerSource ecl: this._ecl)
		{
			Curriculum c = ecl.c;
			
			TreeSet<Class> mandatories = c.getMandatories().get(1);
			TreeSet<Class> electives = c.getElectives();
			
			for(Class cl: mandatories)
			{
				if(!this._mandatories.contains(cl))
					this._mandatories.add(cl);
				
				if(this._electives.contains(cl))
					this._electives.remove(cl);
			}
			
			for(Class cl: electives)
			{
				if(!this._mandatories.contains(cl) && !this._electives.contains(cl))
					this._electives.add(cl);
			}
						
		}
		
		this._joinedCurriculum = new Curriculum(this._mandatories, this._electives);
	}
	
	public Curriculum getCurriculum()
	{
		if(this._joinedCurriculum == null)
			joinCurriculum();
		
		return this._joinedCurriculum;
	}
	
	public StudentsHistory getStudentsHistory()
	{
		HashMap<String, Student> sh = new HashMap<String, Student>();
		
		for(EstimatorContainerSource ecl :this._ecl)
		{
			sh.putAll(ecl.sh.getStudents());
		}
		
		return new StudentsHistory(sh);
	}
	
	public EstimatorContainer2(ArrayList<EstimatorContainerSource> ecs)
	{
		this._ecl = ecs;
		this._result = new HashMap<String, Estimative>();
		this._mandatories = new TreeSet<Class>();
		this._electives = new TreeSet<Class>();
		this._listER = new HashMap<String, EstimativesResult>();
	}
	
	
}

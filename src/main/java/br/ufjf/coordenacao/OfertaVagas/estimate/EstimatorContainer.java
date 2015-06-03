package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class EstimatorContainer {
	private ArrayList<EstimatorContainerSource> _ecs;
	private HashMap<String, EstimativesResult> _listER;
	private HashMap<String, Estimative> _result;
	private TreeSet<Class> _mandatories;
	private TreeSet<Class> _electives;
	private Curriculum _joinedCurriculum;
	
	private void process() throws IOException
	{
		for(EstimatorContainerSource ecs: this._ecs)
		{		
			Estimator estimator = new Estimator(ecs.getCurriculumForMultiple(), ecs.getSh());
			EstimativesResult result = estimator.populateData().process(0.9f, 0.6f, 0.7f, 0.8f, 0.5f);
			this._listER.put(ecs.getCurriculumId(), result);
			
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
	
	public EstimativesResult getResult() throws IOException
	{
		this.process();
		
		EstimativesResult er = new EstimativesResult();
		
		Iterator<Entry<String, Estimative>> it = this._result.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<String, Estimative> e = it.next();
			er.addEstimative(e.getValue());
		}
		
		return er;
	}
	
	public void joinCurriculum() throws IOException{
		for(EstimatorContainerSource ecl: this._ecs)
		{
			Curriculum c = ecl.getCurriculumForMultiple();
			
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
			try {
				joinCurriculum();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return this._joinedCurriculum;
	}
	
	public StudentsHistory getStudentsHistory()
	{
		HashMap<String, Student> sh = new HashMap<String, Student>();
		
		for(EstimatorContainerSource ecl :this._ecs)
		{
			sh.putAll(ecl.getSh().getStudents());
		}
		
		return new StudentsHistory(sh);
	}
	
	public EstimativesResult getResultByCurriculum(String curriculum)
	{	
		return this._listER.get(curriculum);
	}
	
	public Curriculum getCurriculum(String curriculum)
	{
		for(EstimatorContainerSource source :this._ecs)
		{
			if(source.getCurriculumId().equals(curriculum))
				 return source.getCurriculum();
		}
		
		return null;
	}
	
	public EstimatorContainer(ArrayList<EstimatorContainerSource> ecs)
	{
		this._ecs = ecs;
		this._result = new HashMap<String, Estimative>();
		this._mandatories = new TreeSet<Class>();
		this._electives = new TreeSet<Class>();
		this._listER = new HashMap<String, EstimativesResult>();
	}
}
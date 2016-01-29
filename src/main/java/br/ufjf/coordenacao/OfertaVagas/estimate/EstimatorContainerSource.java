package br.ufjf.coordenacao.OfertaVagas.estimate;

import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class EstimatorContainerSource {
	private StudentsHistory sh;
	private Curriculum c;
	private String curriculumid;
	
	public String getCurriculumId() {
		return curriculumid;
	}

	public void setCurriculumId(String curriculumid) {
		this.curriculumid = curriculumid;
	}

	public Curriculum getCurriculumForMultiple()
	{
		return c.getCurriculumForMultiples();
	}
	
	public Curriculum getCurriculum() {
		return c;
	}

	public void setCurriculum(Curriculum c) {
		this.c = c;
	}

	public StudentsHistory getSh() {
		return sh;
	}

	public void setSh(StudentsHistory sh) {
		this.sh = sh;
	}

	public EstimatorContainerSource(StudentsHistory sh, Curriculum c, String cur)
	{
		this.setSh(sh);
		this.setCurriculum(c);
		this.curriculumid = cur;
	}
}
package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.util.ArrayList;

import br.ufjf.coordenacao.OfertaVagas.model.Class;

public class EstimativesResult {

	private ArrayList<Estimative> estimatives = new ArrayList<Estimative>();
	
	public EstimativesResult() {	}
	
	
	public void addEstimative(Class c, int hasPreReq, int canPreReq, int enrolled, int reprovedGrade, int reprovedFrequency) {
		this.estimatives.add(new Estimative(c.getId(), hasPreReq, canPreReq, enrolled, reprovedGrade, reprovedFrequency));
	}
	
	@Override
	public String toString() {
		String out = "";
		for (Estimative est : this.estimatives) {
			out += est.getClassId() + 
					" = [" + est.getQtdHasAllPrereq() + 
					", " + est.getQtdCanHaveAllPreq() + ", " + 
					est.getQtdEnrolled() + "]\n";
		}
		return out;
	}
	
	public EstimativesResult process(float w1, float w2, float w3, float w4, float w5) {
		
		for (Estimative est : this.estimatives) {
			est.setQtdHasAllPrereqWeight(w1);
			est.setQtdEnrolledWeight(w2);
			est.setQtdCanHaveAllPreqWeight(w3);
			est.setQtdReprovedFreqWeight(w4);
			est.setQtdReprovedGradeWeight(w5);
		}
		
		return this;
	}
	
	public ArrayList<Estimative> getEstimatives() {
		return estimatives;
	}
	
}

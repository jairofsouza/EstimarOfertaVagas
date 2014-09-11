package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.util.ArrayList;

import br.ufjf.coordenacao.OfertaVagas.model.Course;

public class EstimativesResult {

	private ArrayList<Estimative> estimatives = new ArrayList<Estimative>();
	
	public EstimativesResult() {	}
	
	
	public void addEstimative(Course c, int hasPreReq, int canPreReq, int enrolled) {
		this.estimatives.add(new Estimative(c.getId(), hasPreReq, canPreReq, enrolled));
	}
	
	@Override
	public String toString() {
		String out = "";
		for (Estimative est : this.estimatives) {
			out += est.getCourseId() + 
					" = [" + est.getQtdHasAllPrereq() + 
					", " + est.getQdtCanHaveAllPreq() + ", " + 
					est.getQtdEnrolled() + "]\n";
		}
		return out;
	}
	
	public EstimativesResult process(float w1, float w2, float w3) {
		
		for (Estimative est : this.estimatives) {
			est.setQtdHasAllPrereqWeighted((int) Math.ceil(est.getQtdHasAllPrereq()*w1));
			est.setQtdEnrolledWeighted((int)Math.ceil(est.getQtdEnrolled()*w2));
			est.setQdtCanHaveAllPreqWeighted((int)Math.ceil(est.getQdtCanHaveAllPreq()*w3));
			est.setQtdTotal(est.getQdtCanHaveAllPreqWeighted()+est.getQtdEnrolledWeighted()+est.getQtdHasAllPrereqWeighted());
		}
		
		return this;
	}
	
	public ArrayList<Estimative> getEstimatives() {
		return estimatives;
	}
	
}

package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufjf.coordenacao.OfertaVagas.model.Course;
import br.ufjf.coordenacao.OfertaVagas.model.CourseStatus;
import br.ufjf.coordenacao.OfertaVagas.model.Student;

public class EstimativesResult {

//	private HashMap<Course, HashMap<CourseStatus, Student>> dataByCourse = new HashMap<Course, HashMap<CourseStatus, Student>>();
	
//	private HashMap<CourseType, TreeSet<Course>> estApproved = new HashMap<CourseType, TreeSet<Course>>();
	
	
	private ArrayList<Estimative> estimatives = new ArrayList<Estimative>();
	
	public EstimativesResult() {
		// TODO Auto-generated constructor stub
	}
	
	
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
	
}

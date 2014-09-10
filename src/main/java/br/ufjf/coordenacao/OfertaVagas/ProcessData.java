package br.ufjf.coordenacao.OfertaVagas;

import java.io.File;
import java.io.IOException;

import br.ufjf.coordenacao.OfertaVagas.estimate.Estimator;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class ProcessData {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		//TODO receber esses argumentos da função principal
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader(
				new File("data/grade_obrigatorias.txt"),
				new File("data/grade_eletivas.txt"),
				new File("data/equivalencias.txt"));
		Curriculum c = csvcur.getCurriculum();
		
//		System.out.println(CourseFactory.makeString());
	
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/alunos35A.csv"));
		StudentsHistory sh = csv.getStudentsHistory();
//		System.out.println(sh);

		Estimator estimator = new Estimator(c, sh);
		System.out.println(estimator.populateData());
		
	}

}

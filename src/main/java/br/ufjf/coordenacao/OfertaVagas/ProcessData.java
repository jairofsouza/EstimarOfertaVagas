package br.ufjf.coordenacao.OfertaVagas;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Calendar;

import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;
import br.ufjf.coordenacao.OfertaVagas.estimate.Estimator;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.report.CSVReport;
import br.ufjf.coordenacao.OfertaVagas.report.HTMLDetailedReport;
import br.ufjf.coordenacao.OfertaVagas.report.HTMLReport;

public class ProcessData {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		//TODO receber esses argumentos da função principal
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader(
				new File("data/76A_grade_obrigatorias.txt"),
				new File("data/76A_grade_eletivas.txt"),
				new File("data/76A_equivalencias.txt"));
		Curriculum c = csvcur.getCurriculum();
		
//		System.out.println(CourseFactory.makeString());
	
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/alunos76A.csv"));
		StudentsHistory sh = csv.getStudentsHistory();
//		System.out.println(sh);

		Estimator estimator = new Estimator(c, sh);
		EstimativesResult result = estimator.populateData().process(0.9f, 0.6f, 0.7f, 0.8f, 0.5f);

		File file = new File("data/resultado76A_"+Calendar.getInstance().getTimeInMillis()+".html");
		file.createNewFile();
		PrintStream ps = new PrintStream(file); 
		new HTMLDetailedReport(ps).generate(result, sh, c);
		
	}

}

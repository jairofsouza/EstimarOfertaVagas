package br.ufjf.coordenacao.OfertaVagas;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;
import br.ufjf.coordenacao.OfertaVagas.estimate.Estimator;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.StudentGradeFilter;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.report.HTMLDetailedReport;

public class ProcessData {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String ano = "2014";
		String curriculo = "12014";
		String curso = "35A";
		
		//TODO receber esses argumentos da função principal
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader(curso, curriculo,
				new File("data/35A_grade_obrigatorias_"+ano+".txt"),
				new File("data/35A_eletivas_"+ano+".txt"),
				new File("data/35A_equivalencias.txt"));

		Curriculum c = csvcur.getCurriculum();
		
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/historicos_2015.3.csv"), new StudentGradeFilter(curriculo));
		StudentsHistory sh = csv.getStudentsHistory();

		Estimator estimator = new Estimator(c, sh);
		EstimativesResult result = estimator.populateData().process(0.9f, 0.6f, 0.7f, 0.8f, 0.5f);
		
		System.out.println(c.getCurriculumForMultiples());
		if(!new File("data/result/").exists())
		{
			new File("data/result/").mkdirs();
		}
		
		File file = new File("data/result/resultado"+curso+"_"+Calendar.getInstance().getTimeInMillis()+"_"+curriculo+"_.html");
		file.createNewFile();
		PrintStream ps = new PrintStream(file); 
		new HTMLDetailedReport(ps).generate(result, sh, c);
		}

}

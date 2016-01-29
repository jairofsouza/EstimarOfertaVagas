package exemplo;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;
import br.ufjf.coordenacao.OfertaVagas.estimate.Estimator;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CurriculumElectivesFilter;

import br.ufjf.coordenacao.OfertaVagas.loader.StudentGradeFilter;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.report.HTMLDetailedReport;


public class filtroGrade {

	/*
	 * Classe de exemplo para testar o filtro de periodos.
	 */

	public static void main(String args[]) throws IOException
	{

		String curso = "35A", 
			   curriculo = "12014";
		
		/*
		 * O construtor da classe CSVCurriculumLoader usa o arquivo de obrigatorias, eletivas e equivalencias da disciplina como parametros obrigatorios. O filtro e adicionado
		 * no proximo parametro. ƒ possivel utilizar qualquer CurriculumFilter nesse campo (CurriculumDisciplineFiler, CurriculumElectiveFilter, ...).
		 * Se nao for passado nenhum filtro, o construtor usa automaticamente o NoFilter, onde nenhum filtro e aplicado.
		 */
		
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader(curso, curriculo,
				new File("data/35A_grade_obrigatorias_2014.txt"),
				new File("data/35A_eletivas_2014.txt"),
				new File("data/35A_equivalencias.txt"), new CurriculumElectivesFilter());

		Curriculum c = csvcur.getCurriculum();
		
		/*
		 * A classe CSVCurriculumLoader e construido com o arquivo de alunos. Tambem e possivel usar qualquer StudentFilter (StudentGradeFilter, StudentIngressFilter)
		 * como proximo parametro do arquivo. Se nenhum filtro for aplicado, a classe tambem utiliza o NoFilter automaticamente.
		 */
		
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"), new StudentGradeFilter("12014"));
		StudentsHistory sh = csv.getStudentsHistory();

		Estimator estimator = new Estimator(c, sh);
		EstimativesResult result = estimator.populateData().process(0.9f, 0.6f, 0.7f, 0.8f, 0.5f);

		File file = new File("data/result/resultado35A_filtro_"+Calendar.getInstance().getTimeInMillis()+".html");
		file.createNewFile();
		PrintStream ps = new PrintStream(file); 
		new HTMLDetailedReport(ps).generate(result, sh, c);
		
	}
}
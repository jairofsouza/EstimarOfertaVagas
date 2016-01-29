package exemplo;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;
import br.ufjf.coordenacao.OfertaVagas.estimate.EstimatorContainer;
import br.ufjf.coordenacao.OfertaVagas.estimate.EstimatorContainerSource;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.StudentGradeFilter;
import br.ufjf.coordenacao.OfertaVagas.model.ClassFactory;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.report.HTMLDetailedReport;
public class MultiplasGrades {

		/**
		 * @param args
		 * @throws IOException 
		 */
		
		/*
		 * Classe de teste de multiplas grades utilizando a classe EstimatorContainer
		 *	Utilizando mais de uma grade. Aqui serao utilizados 3 grades direferntes
		 *  e um arquivo com todos os alunos.
		 */
		public static void main(String[] args) throws IOException {
				
			/*
			 * Para carregar a grade é passado a grade de obrigatorias, eletivas e equivalencias.
			 * O parametro "true", serve para indicar que a grade que o CSVCurriculumLoader
			 * irá gerar será utilizada para processar multiplas grades.
			 * 
			 * ----!!!!ATENÇÃO!!!!-----
			 * Para não causar problemas com as equivalências, AS GRADES DE DISCIPLINAS DEVEM SER LIDAS ANTES
			 * DO HISTORICO DOS ALUNOS.
			*/
						
			CSVCurriculumLoader csvcur14 = new CSVCurriculumLoader("35A", "12014",
					new File("data/35A_grade_obrigatorias_2014.txt"),
					new File("data/35A_eletivas_2014.txt"),
					new File("data/35A_equivalencias.txt"));
			Curriculum c14 = csvcur14.getCurriculum();
			
			CSVCurriculumLoader csvcur09 = new CSVCurriculumLoader("35A", "12009",
					new File("data/35A_grade_obrigatorias_2009.txt"),
					new File("data/35A_eletivas_2009.txt"),
					new File("data/35A_equivalencias.txt"));
			Curriculum c09 = csvcur09.getCurriculum();
			
			/*
			 * Para carregar os alunos, o CSVStudentLoader recebe o arquivo .csv com os dados
			 * dos alunos e um parametro String que define um filtro de grade, onde somente
			 * os estudantes pertencentes aquela grade sao carregados.
			 */
			CSVStudentLoader st14 = new CSVStudentLoader(new File("data/historicos_2015.3.csv"), new StudentGradeFilter("12014"));
			StudentsHistory sh14 = st14.getStudentsHistory();
			
						
			CSVStudentLoader st09 = new CSVStudentLoader(new File("data/historicos_2015.3.csv"), new StudentGradeFilter("12009"));
			StudentsHistory sh09 = st09.getStudentsHistory();
						
			
			/*
			 * O EstimatorContainerSource e uma classe que agrupa os alunos e as disciplinas
			 * de uma grade. Aqui as grades sao separadas e cada uma tem o seu EstimatorContainerSource
			 * contendo sua grade, os alunos pertencentes a ela e o código da grade.
			 */
			
			EstimatorContainerSource ecs = new EstimatorContainerSource(sh14, c14, "12014");
			EstimatorContainerSource ecs2 = new EstimatorContainerSource(sh09, c09, "22004");
			
			ArrayList<EstimatorContainerSource> list = new ArrayList<EstimatorContainerSource>();
			list.add(ecs);
			list.add(ecs2);
			/*
			 *O EstimatorContainer utiliza uma lista (ArrayList) de EstimatorContainerSurce
			 *como fonte de dados.  
			 */
			EstimatorContainer ec = new EstimatorContainer(list);
			EstimativesResult er = ec.populateData().process(0.9f, 0.6f, 0.7f, 0.8f, 0.5f);
			if(!new File("data/result/").exists())
			{
				new File("data/result/").mkdirs();
			}
			
			File file = new File("data/result/resultado35A_multiplasGrades_"+Calendar.getInstance().getTimeInMillis()+".html");
			file.createNewFile();
			PrintStream ps = new PrintStream(file); 
			new HTMLDetailedReport(ps).generate(er, ec.getStudentsHistory(), ec.getCurriculum());
		}
			
	}
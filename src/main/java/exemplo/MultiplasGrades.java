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
				
			HashMap<String, CSVCurriculumLoader> csvcur = new HashMap<String, CSVCurriculumLoader>();
			
			/*
			 * Para carregar a grade Ž passado a grade de obrigatorias, eletivas e equivalencias.
			 * O parametro "true", serve para indicar que a grade que o CSVCurriculumLoader
			 * ir‡ gerar ser‡ utilizada para processar multiplas grades.
			 * 
			 * Apos criado, o csvcur foi colocado em um HashMap com o nome da grade.
			*/
						
			CSVCurriculumLoader csvcur1 = new CSVCurriculumLoader(
					new File("data/35A_grade_obrigatorias_2014.txt"),
					new File("data/35A_eletivas_2014.txt"),
					new File("data/35A_equivalencias.txt"));
			csvcur.put("12014", csvcur1);
			
			CSVCurriculumLoader csvcur2 = new CSVCurriculumLoader(
					new File("data/35A_grade_obrigatorias_2009.txt"),
					new File("data/35A_eletivas_2009.txt"),
					new File("data/35A_equivalencias.txt"));
			csvcur.put("22004", csvcur2);
			
			CSVCurriculumLoader csvcur3 = new CSVCurriculumLoader(
					new File("data/35A_grade_obrigatorias_2009.txt"),
					new File("data/35A_eletivas_2009.txt"),
					new File("data/35A_equivalencias.txt"));
			csvcur.put("12009", csvcur3);
			
			/*
			 * Para carregar os alunos, o CSVStudentLoader recebe o arquivo .csv com os dados
			 * dos alunos e um parametro String que define um filtro de grade, onde somente
			 * os estudantes pertencentes aquela grade sao carregados.
			 */
			CSVStudentLoader st1 = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"), new StudentGradeFilter("12014"));
			StudentsHistory sh1 = st1.getStudentsHistory();
			
						
			CSVStudentLoader st2 = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"), new StudentGradeFilter("22004"));
			StudentsHistory sh2 = st2.getStudentsHistory();
						
			CSVStudentLoader st3 = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"), new StudentGradeFilter("12009"));
			StudentsHistory sh3 = st3.getStudentsHistory();
			
			/*
			 * O EstimatorContainerSource e uma classe que agrupa os alunos e as disciplinas
			 * de uma grade. Aqui as grades sao separadas e cada uma tem o seu EstimatorContainerSource
			 * contendo sua grade e os alunos pertencentes a ela. 
			 */
			EstimatorContainerSource ecs = new EstimatorContainerSource(sh1, csvcur.get("12014"), "1204");
			EstimatorContainerSource ecs2 = new EstimatorContainerSource(sh2, csvcur.get("22004"), "1204");
			EstimatorContainerSource ecs3 = new EstimatorContainerSource(sh3, csvcur.get("12009"), "1204");
			
			ArrayList<EstimatorContainerSource> list = new ArrayList<EstimatorContainerSource>();
			list.add(ecs);
			list.add(ecs2);
			list.add(ecs3);
			
			/*
			 *O EstimatorContainer utiliza uma lista (ArrayList) de EstimatorContainerSurce
			 *como fonte de dados.  
			 */
			EstimatorContainer ec = new EstimatorContainer(list);
			EstimativesResult er = ec.getResult();
			
			File file = new File("data/result/resultado35A_"+Calendar.getInstance().getTimeInMillis()+".html");
			file.createNewFile();
			PrintStream ps = new PrintStream(file); 
			new HTMLDetailedReport(ps).generate(er, ec.getStudentsHistory(), ec.getCurriculum());
		}
			
	}
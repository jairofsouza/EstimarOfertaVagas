package br.ufjf.coordenacao.OfertaVagas;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import br.ufjf.coordenacao.OfertaVagas.estimate.Estimative;
import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;
import br.ufjf.coordenacao.OfertaVagas.estimate.EstimatorContainer2;
import br.ufjf.coordenacao.OfertaVagas.estimate.EstimatorContainerSource;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.report.HTMLDetailedReport;

public class ProcessMultipleData {

		/**
		 * @param args
		 * @throws IOException 
		 */
		//public static void process(HashMap<String, CSVCurriculumLoader> csvcur, File students) throws IOException {
		public static void main(String[] args) throws IOException {
				
			HashMap<String, CSVCurriculumLoader> csvcur = new HashMap<String, CSVCurriculumLoader>();
			
			CSVCurriculumLoader csvcur1 = new CSVCurriculumLoader(
					new File("data/35A_grade_obrigatorias_2014.txt"),
					new File("data/35A_eletivas_2014.txt"),
					new File("data/35A_equivalencias.txt"), true);
			
			CSVCurriculumLoader csvcur2 = new CSVCurriculumLoader(
					new File("data/35A_grade_obrigatorias_2009.txt"),
					new File("data/35A_eletivas_2009.txt"),
					new File("data/35A_equivalencias.txt"), true);

			CSVCurriculumLoader csvcur3 = new CSVCurriculumLoader(
					new File("data/35A_grade_obrigatorias_2009.txt"),
					new File("data/35A_eletivas_2009.txt"),
					new File("data/35A_equivalencias.txt"), true);

			csvcur.put("12014", csvcur1);
			csvcur.put("22004", csvcur2);
			csvcur.put("12009", csvcur3);
			
			CSVStudentLoader st1 = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"), "12014");
			StudentsHistory sh1 = st1.getStudentsHistory();
			EstimatorContainerSource ecs = new EstimatorContainerSource(sh1, csvcur.get("12014").getCurriculum());
			System.out.println(sh1);
			
			CSVStudentLoader st2 = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"), "22004");
			StudentsHistory sh2 = st2.getStudentsHistory();
			EstimatorContainerSource ecs2 = new EstimatorContainerSource(sh2, csvcur.get("22004").getCurriculum());
			System.out.println(sh2);
			
			CSVStudentLoader st3 = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"), "12009");
			StudentsHistory sh3 = st3.getStudentsHistory();
			EstimatorContainerSource ecs3 = new EstimatorContainerSource(sh3, csvcur.get("12009").getCurriculum());
			System.out.println(sh3);
			
			ArrayList<EstimatorContainerSource> list = new ArrayList<EstimatorContainerSource>();
			list.add(ecs);
			list.add(ecs2);
			list.add(ecs3);
			
			EstimatorContainer2 ec = new EstimatorContainer2(list);
			EstimativesResult er = ec.getResult();
			
			System.out.println(ecs.c);
			System.out.println("\n\n ---- \n");
			System.out.println(ecs2.c);
			System.out.println("\n\n ---- \n");
			System.out.println(ecs3.c);
			
			File file = new File("data/resultado35A_"+Calendar.getInstance().getTimeInMillis()+".html");
			file.createNewFile();
			PrintStream ps = new PrintStream(file); 
			new HTMLDetailedReport(ps).generate(er, ec.getStudentsHistory(), ec.getCurriculum());
			
			List<Estimative> L_est = new ArrayList<Estimative>();
			L_est = er.getEstimatives();
			
			System.out.println("\nFinalizado---");
			for(Estimative est: L_est)
			{
				//if(est.getClassId().equals("MAT154") || est.getClassId().equals("MAT155") || est.getClassId().equals("DCC121"))
					System.out.println(est);
				
			}
		}

	}


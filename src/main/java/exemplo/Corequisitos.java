package exemplo;

import java.io.File;
import java.io.IOException;

import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.report.StudentCoursePlan;

public class Corequisitos {

	public static void main(String[] args) throws  IOException
	{
		/*
		 * o arquivo dos corequisitos deve ser passado no construtor da classe
		 * CSVCurriculumLoader
		 */
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader("35A", "12014",
				new File("data/35A_grade_obrigatorias_2014.txt"),
				new File("data/35A_eletivas_2014.txt"),
				new File("data/35A_equivalencias.txt"),
				new File("data/35A_corequisitos.txt"));
		Curriculum c = csvcur.getCurriculum();
		
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"));
		StudentsHistory sh = csv.getStudentsHistory();
		
		Student s = sh.getStudents().get("268580001,3");
		
		StudentCoursePlan g = new StudentCoursePlan(s, c, 300, false, true);
		Curriculum cur2 = g.generate();

		for(int i : cur2.getMandatories().keySet())
		{
			System.out.print("\n" + i + ": ");

			for(Class cl: cur2.getMandatories().get(i))
			{
				System.out.print(cl.getId() + " ");
			}
		}

		System.out.println("\n---------");
	}
}

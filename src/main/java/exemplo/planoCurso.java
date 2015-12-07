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

public class planoCurso {

	public static void main(String[] args) throws IOException
	{
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader(
				new File("data/35A_grade_obrigatorias_2009.txt"),
				new File("data/35A_eletivas_2009.txt"),
				new File("data/35A_equivalencias.txt"));
		Curriculum c = csvcur.getCurriculum();

		CSVStudentLoader csv = new CSVStudentLoader(new File("data/historicos_2015.3.csv"));
		StudentsHistory sh = csv.getStudentsHistory();

		Student st = sh.getStudents().get("200935049");

		StudentCoursePlan g = new StudentCoursePlan(st, c, 300, false, true);
		Curriculum cur = g.generate();

		for(int i : cur.getMandatories().keySet())
		{
			System.out.print("\n" + i + ": ");

			for(Class cl: cur.getMandatories().get(i))
			{
				System.out.print(cl.getId() + " ");
			}

		}

		System.out.println("\n---------");
	}
}
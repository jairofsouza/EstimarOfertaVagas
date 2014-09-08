package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.io.IOException;

import br.ufjf.coordenacao.OfertaVagas.model.CourseFactory;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class LoaderTest {
	public static void main(String[] args) throws IOException {
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader(
				new File("data/grade_obrigatorias.txt"),
				new File("data/grade_eletivas.txt"),
				new File("data/equivalencias.txt"));
		Curriculum c = csvcur.getCurriculum();
		
//		System.out.println(CourseFactory.makeString());
	
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/alunos.txt"));
		StudentsHistory sh = csv.getStudentsHistory();
		System.out.println(sh);

	
	}
}

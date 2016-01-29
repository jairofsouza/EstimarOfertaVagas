package exemplo;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.report.StudentReport;

public class FichaAluno {
	
	public static void main(String[] args) throws IOException
	{
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader("35A", "12014",
				new File("data/35A_grade_obrigatorias_2014.txt"),
				new File("data/35A_eletivas_2014.txt"),
				new File("data/35A_equivalencias.txt"));
		
		Curriculum c = csvcur.getCurriculum();
	
		
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/historicos_2015.3.csv"));
		StudentsHistory sh = csv.getStudentsHistory();
		
		Student st = sh.getStudents().get("201435001");
		
		if(!new File("data/result/").exists())
		{
			new File("data/result/").mkdirs();
		}
		
		File file =  new File("data/result/Ficha_" + st.getId() + "_"+ System.currentTimeMillis() + ".html");
		file.createNewFile();
		PrintStream ps = new PrintStream(file); 
		new StudentReport(ps).generate(st, c);
	}

}
package exemplo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.*;
import br.ufjf.coordenacao.OfertaVagas.model.Class;

public class CalculoIRA {
	/*
	 * Classe de exemplo que mostra como obter o IRA de um aluno.
	 * √â possivel obter o IRA total do aluno, o de um semestre especifico ou
	 * das disciplinas de um semestre
	 */
	public static void main(String[] args) throws IOException
	{
		String ano = "2009";
		String curriculo = "12009";
		String curso = "35A";
		String aluno = "201335006";
		String[] pulaDisciplinas = {};
		
		/*
		 * Carrregando as grades de curriculo.
		 * OBS: As disciplinas que são desconsideradas para o cálculo do IRA são atributos da grade do currículo
		 * e deve ser configurada na grade em que o alno está cursando.
		 */
		CSVCurriculumLoader csvcur = new CSVCurriculumLoader(curso, curriculo,
				new File("data/35A_grade_obrigatorias_"+ano+".txt"),
				new File("data/35A_eletivas_"+ano+".txt"),
				new File("data/35A_equivalencias.txt"));
		
		Curriculum c = csvcur.getCurriculum();
		
		TreeSet<String> skip = new TreeSet<String>(Arrays.asList(pulaDisciplinas));
		
		c.setIRA_skipClasses(skip);
		
		//Carregando o historico de alunos
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/historicos_2015.3.csv"));
		StudentsHistory sh = csv.getStudentsHistory();
		
		//Selecionando um aluno qualquer e imprime as informa√ß√µes dele
		Student st = sh.getStudents().get(aluno);
		System.out.println("Aluno: " + st.getNome() + "\tMatricula:" + st.getId() + "\tIngresso: " + st.getFirstSemester() +"\n");
		
		//Imprime o IRA total do aluno (do primeiro ate o ultimo semestre que ele cursou
		System.out.println("IRA geral: " + st.getIRA() + "\n");
		
		/* Pega todos os semestres em que o aluno esteve matriculado em uma disciplina e mostra o IRA acumulado do aluno
		 * naquele semestre e entre parenteses o IRA das disciplinas que ele cursou naquele semestre.
		 */
		TreeSet<Integer> semestres = st.getCoursedSemesters();
		for(int i: semestres)
		{
			System.out.println("IRA em " + i + ": " + st.getIRA(i) + " (" + st.getSemesterIRA(i) + ")");
		}
	}
}

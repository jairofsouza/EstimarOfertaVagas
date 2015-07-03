package exemplo;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class CalculoIRA {
	/*
	 * Classe de exemplo que mostra como obter o IRA de um aluno.
	 * É possivel obter o IRA total do aluno, o de um semestre especifico ou
	 * das disciplinas de um semestre
	 */
	public static void main(String[] args) throws IOException
	{
		//Carregando o historico de alunos
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"));
		StudentsHistory sh = csv.getStudentsHistory();
		
		//Selecionando um aluno qualquer e imprime as informações dele
		Student st = sh.getStudents().get("267246696");
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

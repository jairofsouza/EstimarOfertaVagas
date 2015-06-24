package exemplo;

import java.io.File;
import java.io.IOException;

import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class CalculoIRA {

	/*
	 * Classe de exemplo que mostra como obter o IRA de um aluno.
	 * ƒ possivel obter o IRA total do aluno, o de um semestre especifico ou
	 * das disciplinas de um semestre
	 */
	public static void main(String[] args) throws IOException
	{
		//Carregando o historico de alunos
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/35A_alunos_2014.csv"));
		StudentsHistory sh = csv.getStudentsHistory();
		
		//Selecionando um aluno qualquer
		Student st = sh.getStudents().get("268580030,7");
		
		float ira1 = st.getIRA(); //Obtem o IRA total do aluno
		float ira2 = st.getIRA(20131); //Obtem o IRA do aluno em um semestre (todas as disciplinas cursadas do 1o ate o semestre informado) (20131)
		float ira3 = st.getSemesterIRA(20103); //Obtem o IRA do aluno com apenas as disciplinas que ele cursou naquele semestre (20103)
		
		System.out.println("ALUNO: " + st.getId() + "\n-IRA total:" + ira1 + "\t-IRA em 20131: " + ira2 + "\t-IRA das disc. de 20103: " + ira3);
	}
}

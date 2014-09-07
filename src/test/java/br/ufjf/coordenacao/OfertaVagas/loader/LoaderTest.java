package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.io.IOException;

import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class LoaderTest {
	public static void main(String[] args) throws IOException {
		CSVStudentLoader csv = new CSVStudentLoader(new File("data/alunos.txt"));
		StudentsHistory sh = csv.getStudentsHistory();
		System.out.println(sh);
	}
}

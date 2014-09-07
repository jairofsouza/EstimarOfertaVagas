package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.IOException;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public interface IStudentLoader {

	public StudentsHistory getStudentsHistory() throws IOException;
	
}

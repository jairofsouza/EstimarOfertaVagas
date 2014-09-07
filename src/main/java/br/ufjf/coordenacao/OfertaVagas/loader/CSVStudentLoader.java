package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import br.ufjf.coordenacao.OfertaVagas.model.Student;

public class CSVStudentLoader implements IStudentLoader {

	private File _file;
	
	public CSVStudentLoader(File CSVfile) {
		this._file = CSVfile;
	}
	
	public Collection<Student> getStudentsHistory() {
		return new ArrayList<Student>();
	}
	
}
	
	
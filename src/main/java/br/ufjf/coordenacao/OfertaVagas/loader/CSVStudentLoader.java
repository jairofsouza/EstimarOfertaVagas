package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.ufjf.coordenacao.OfertaVagas.model.CourseStatus;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class CSVStudentLoader implements IStudentLoader {

	private File _file;
	
	public CSVStudentLoader(File CSVfile) {
		this._file = CSVfile;
	}
	
	public StudentsHistory getStudentsHistory() throws IOException {
		
		Reader in = new FileReader(this._file);
		Iterable<CSVRecord> records = CSVFormat.newFormat(';').parse(in);
		
		StudentsHistory sh = new StudentsHistory();
		CourseStatus status;
		
		for (CSVRecord record : records) {
		    String courseStatus = record.get(6).trim(); //Aprovado ou cursando
		    
		    if (courseStatus.equals("Matriculado")) status = CourseStatus.ENROLLED;
		    else if (courseStatus.equals("Aprovado") || courseStatus.equals("Dispensado")) status = CourseStatus.APPROVED;
		    else continue; // do nothing
		    	
		    this.add(sh,
		    		record.get(1).trim(), // matricula 
		    		record.get(4).trim(), // disciplina
		    		status // cursando ou aprovado
		    	);
		}
		
		return sh;
	}

	private void add(StudentsHistory sh, String id, String course, CourseStatus status) {
    	sh.add(id, course, status);
	}
	
}
	
	
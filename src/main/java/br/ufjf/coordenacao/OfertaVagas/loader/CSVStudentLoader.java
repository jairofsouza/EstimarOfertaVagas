package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;



/**
 * ƒ pre-requisito aqui que o arquivo de entrada esteja ordenado por semestre (ou seja, semestres antigos devem aparecer primeiro que semestres mais atuais)
 * @author Jairo
 *
 */
public class CSVStudentLoader implements IStudentLoader {

	private File _file;
	
	public CSVStudentLoader(File CSVfile) {
		this._file = CSVfile;
	}
	
	public StudentsHistory getStudentsHistory() throws IOException {
		
		Reader in = new FileReader(this._file);
		Iterable<CSVRecord> records = CSVFormat.newFormat(';').parse(in);
		
		StudentsHistory sh = new StudentsHistory();
		ClassStatus status;
		
		for (CSVRecord record : records) {
		    String classStatus = record.get(7).trim();
		    
		    if (classStatus.equals("Matriculado")) status = ClassStatus.ENROLLED;
		    else if (classStatus.equals("Aprovado") || classStatus.equals("Dispensado")) status = ClassStatus.APPROVED;
		    else if (classStatus.equals("Rep Nota")) status = ClassStatus.REPROVED_GRADE;
		    else if (classStatus.equals("Rep Freq")) status = ClassStatus.REPROVED_FREQUENCY;
		    else continue; // do nothing
		    	
		    this.add(sh,
		    		record.get(1).trim(), // matricula 
		    		record.get(2).trim(), // nome
		    		record.get(4).trim(), // semestre cursado
		    		record.get(5).trim(), // disciplina
		    		status // cursando ou aprovado
		    	);
		}
		
		return sh;
	}

	private void add(StudentsHistory sh, String id, String nome, String semester, String _class, ClassStatus status) {
    	sh.add(id, nome, semester, _class, status);
	}
	
}
	
	
package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.ufjf.coordenacao.OfertaVagas.model.Course;
import br.ufjf.coordenacao.OfertaVagas.model.CourseFactory;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;

public class CSVCurriculumLoader implements ICurriculumLoader {

	private File _mandatoryFile;
	private File _electiveFile;
	private File _equivalenceFile;
	Curriculum _cur;
	
	public CSVCurriculumLoader(File mandatoryFile, File electiveFile, File equivalenceFile) {
		this._mandatoryFile = mandatoryFile;
		this._electiveFile = electiveFile;
		this._equivalenceFile = equivalenceFile;
		
		this._cur = new Curriculum();
	}
	
	public Curriculum getCurriculum() throws IOException {
		
		loadMandatoryFile();

		if(this._electiveFile != null) 		loadElectiveFile();
		
		// É necessário que o processamento de equivalências seja feito ao final da carga de eletivas + obrigatórias
		if(this._equivalenceFile != null)	loadEquivalenceFile();

		return _cur;
	}

	private void loadMandatoryFile() throws IOException {
		Reader in = new FileReader(this._mandatoryFile);
		Iterable<CSVRecord> mandatoryRecords = CSVFormat.EXCEL.parse(in);
		
		for (CSVRecord record : mandatoryRecords) {
		    String semester = record.get(0).trim(); //Periodo
		    String course   = record.get(1).trim(); //Disciplina
		    
		    Course c = CourseFactory.getCourse(course);
		    this._cur.addMandatoryCourse(Integer.valueOf(semester), c);
		    
		    for (int i = 2; i < record.size(); i++) {
		    	String prerequisite = record.get(i).trim(); //Pré-requisito
		    	Course pre = CourseFactory.getCourse(prerequisite);
		    	c.addPrerequisite(pre);		    	
		    }

		}
		in.close();
	}

	private void loadElectiveFile() throws IOException {
		Reader in = new FileReader(this._electiveFile);
		Iterable<CSVRecord> electiveRecords = CSVFormat.EXCEL.parse(in);
		
		for (CSVRecord record : electiveRecords) {
		    Course c = CourseFactory.getCourse(record.get(0).trim());
		    this._cur.addElectiveCourse(c);
		    
		    for (int i = 1; i < record.size(); i++) {
		    	String prerequisite = record.get(i).trim(); //Pré-requisito
		    	Course pre = CourseFactory.getCourse(prerequisite);
		    	c.addPrerequisite(pre);		    	
		    }
		}
		in.close();

	}
	
	private void loadEquivalenceFile() throws IOException {
		Reader in = new FileReader(this._equivalenceFile);
		Iterable<CSVRecord> eqRecords = CSVFormat.EXCEL.parse(in);
		
		for (CSVRecord record : eqRecords) {
			String idDaGrade = record.get(0).trim();
		    String idNaoDaGrade = record.get(1).trim();

		    Course c = null;
		    if(!CourseFactory.contains(idDaGrade) && CourseFactory.contains(idNaoDaGrade)) {
		    	String aux = idNaoDaGrade;
		    	idNaoDaGrade = idDaGrade;
		    	idDaGrade = aux;
		    }
		    
		    else if(CourseFactory.contains(idDaGrade) && CourseFactory.contains(idNaoDaGrade))
			    throw new IOException("Equivalência de duas disciplinas já existentes na grade: " + idDaGrade + " <-> " + idNaoDaGrade);

			else if(!CourseFactory.contains(idDaGrade) && !CourseFactory.contains(idNaoDaGrade))
				throw new IOException("Equivalência de duas disciplinas não existentes na grade: " + idDaGrade + " <-> " + idNaoDaGrade);
			   
		    c = CourseFactory.getCourse(idDaGrade);
		    CourseFactory.addCourse(idNaoDaGrade, c);
		}		    

		in.close();
	}
	
}

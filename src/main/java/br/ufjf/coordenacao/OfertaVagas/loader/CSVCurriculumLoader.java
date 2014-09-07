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
	
	public CSVCurriculumLoader(File mandatoryFile, File electiveFile) {
		this._mandatoryFile = mandatoryFile;
		this._electiveFile = electiveFile;
	}
	
	public Curriculum getCurriculum() throws IOException {
		
		Curriculum cur = new Curriculum();
		loadMandatoryFile(cur);

		if(this._electiveFile != null) loadElectiveFile(cur);

		return cur;
	}

	private void loadMandatoryFile(Curriculum cur) throws IOException {
		Reader in = new FileReader(this._mandatoryFile);
		Iterable<CSVRecord> mandatoryRecords = CSVFormat.EXCEL.parse(in);
		
		for (CSVRecord record : mandatoryRecords) {
		    String semester = record.get(0).trim(); //Periodo
		    String course   = record.get(1).trim(); //Disciplina
		    
		    Course c = CourseFactory.getCourse(course);
		    cur.addMandatoryCourse(Integer.valueOf(semester), c);
		    
		    for (int i = 2; i < record.size(); i++) {
		    	String prerequisite = record.get(i).trim(); //PrŽ-requisito
		    	Course pre = CourseFactory.getCourse(prerequisite);
		    	c.addPrerequisite(pre);		    	
		    }

		}
	}

	private void loadElectiveFile(Curriculum cur) throws IOException {
		Reader in = new FileReader(this._electiveFile);
		Iterable<CSVRecord> electiveRecords = CSVFormat.EXCEL.parse(in);
		
		for (CSVRecord record : electiveRecords) {
		    Course c = CourseFactory.getCourse(record.get(0).trim());
		    cur.addElectiveCourse(c);
		    
		    for (int i = 1; i < record.size(); i++) {
		    	String prerequisite = record.get(i).trim(); //PrŽ-requisito
		    	Course pre = CourseFactory.getCourse(prerequisite);
		    	c.addPrerequisite(pre);		    	
		    }
		}

	}
	
}

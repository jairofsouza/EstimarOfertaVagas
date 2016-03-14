package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
//import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassContainer;
import br.ufjf.coordenacao.OfertaVagas.model.ClassFactory;
import br.ufjf.coordenacao.OfertaVagas.model.CurriculumFactory;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;

public class CSVCurriculumLoader implements ICurriculumLoader {

	private File _mandatoryFile;
	private File _electiveFile;
	private File _equivalenceFile;
	private File _corequisiteFile;
	private Curriculum _cur;
	private boolean _multiple;
	private IFilter _filter;
	
	private String _course = "";
	private String _curriculumId = "";
	

	
	public CSVCurriculumLoader(String course, String curriculumId, File mandatoryFile, File electiveFile,File equivalenceFile, IFilter filter) {
		this(course, curriculumId, mandatoryFile, electiveFile, equivalenceFile, null, filter);

	}

	public CSVCurriculumLoader(String course, String curriculumId, File mandatoryFile, File electiveFile,File equivalenceFile) {
		this(course, curriculumId, mandatoryFile, electiveFile, equivalenceFile, new NoFilter());
	}
	
	public CSVCurriculumLoader(String course, String curriculumId, File mandatoryFile, File electiveFile,File equivalenceFile, File corequisiteFile, IFilter filter)
	{
		this._mandatoryFile = mandatoryFile;
		this._electiveFile = electiveFile;
		this._equivalenceFile = equivalenceFile;
		this._corequisiteFile = corequisiteFile;
		this._filter = filter;
		this._course = course;
		this._curriculumId = curriculumId;
		
		this._multiple = false;
		this._cur = new Curriculum();
	}
	
	public CSVCurriculumLoader(String course, String curriculumId, File mandatoryFile, File electiveFile,File equivalenceFile, File corequisiteFile)
	{
		this(course, curriculumId, mandatoryFile, electiveFile, equivalenceFile, corequisiteFile, new NoFilter());
	}

	private Curriculum processCurriculum(boolean multiple) {
		try {
			this._multiple = multiple;

			loadMandatoryFile();

			if (this._electiveFile != null)
				loadElectiveFile();

			// É necessário que o processamento de equivalências seja feito ao
			// final da carga de eletivas + obrigatórias
			if (this._equivalenceFile != null)
				loadEquivalenceFile();
			
			if(this._corequisiteFile != null)
				loadCorequisite();
		}

		catch (Exception e) {
			e.printStackTrace();
			//System.exit(1);
		}
		
		CurriculumFactory.putCurriculum(_course, _curriculumId, _cur);
		
		return this._cur;

	}

	public Curriculum getCurriculum() {
		return processCurriculum(false);
	}

	public Curriculum getCurriculumForMultiples() {
		return processCurriculum(true);
	}

	private void loadMandatoryFile() throws IOException {
		Reader in = new FileReader(this._mandatoryFile);
		Iterable<CSVRecord> mandatoryRecords = CSVFormat.EXCEL.parse(in);

		for (CSVRecord record : mandatoryRecords) {
			if (!this._filter.check(record))
				continue;

			String semester; // Periodo

			if (this._multiple)
				semester = "1";
			else
				semester = record.get(0).trim();

			String _class = record.get(1).trim(); // Disciplina

			Class c = ClassFactory.getClass(_course, _curriculumId,_class);
			this._cur.addMandatoryClass(Integer.valueOf(semester), c);

			for (int i = 2; i < record.size()-1; i++) {
				String prerequisite = record.get(i).trim(); // Pré-requisito
				Class pre = ClassFactory.getClass(_course, _curriculumId, prerequisite);
				c.addPrerequisite(pre);
			}
			
			c.setWorkload(Integer.valueOf(record.get(record.size()-1).trim()));


		}
		in.close();
	}

	private void loadCorequisite() throws IOException, FileNotFoundException
	{
		Reader in = new FileReader(this._corequisiteFile);
		Iterable<CSVRecord> eqRecords = CSVFormat.EXCEL.parse(in);

		for (CSVRecord record : eqRecords)
		{
			String disciplina = record.get(0).trim();
			String corequisito = record.get(1).trim();
			
			Class d = ClassFactory.getClass(_course, _curriculumId, disciplina);
			Class cr = ClassFactory.getClass(_course, _curriculumId, corequisito);
		
			d.addCorequisite(cr);
		}
		/* MOVIDO PARA O STUDENTCOURSEPLAN
		for(int i: _cur.getMandatories().keySet())
		{
			//for(Class c: _cur.getMandatories().get(i))
			Iterator<Class> it = _cur.getMandatories().get(i).iterator();
			
			ArrayList<Class> duplicated = new ArrayList<Class>();
			ArrayList<Class> insert = new ArrayList<Class>();
			while(it.hasNext())
			{			
				Class c = it.next();
				if(!c.getCorequisite().isEmpty())
				{
					ClassContainer cc = new ClassContainer("Co-requisitos");
					cc.addClass(c);
					
					for(Class cr: c.getCorequisite())
					{
						//_cur.getMandatories().get(i).remove(cr);
						//it.remove();
						duplicated.add(cr);
						cc.addClass(cr);
					}
					
					//_cur.getMandatories().get(i).remove(c);
					it.remove();
					insert.add(cc);
				}
			}
			
			_cur.getMandatories().get(i).removeAll(duplicated);
			_cur.getMandatories().get(i).addAll(insert);
		}
		*/
		in.close();
	}
	
	private void loadElectiveFile() throws IOException {
		Reader in = new FileReader(this._electiveFile);
		Iterable<CSVRecord> electiveRecords = CSVFormat.EXCEL.parse(in);

		// TODO implementar o filtro
		for (CSVRecord record : electiveRecords) {
			if (this._filter.check(record)) {
				Class c = ClassFactory.getClass(_course, _curriculumId, record.get(0).trim());
				this._cur.addElectiveClass(c);

				for (int i = 1; i < record.size()-1; i++) {
					String prerequisite = record.get(i).trim(); // Pré-requisito
					Class pre = ClassFactory.getClass(_course, _curriculumId, prerequisite);
					c.addPrerequisite(pre);
				}
				
				c.setWorkload(Integer.valueOf(record.get(record.size()-1).trim()));
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

			Class c = null;
			if (!ClassFactory.contains(_course, _curriculumId, idDaGrade)
					&& ClassFactory.contains(_course, _curriculumId, idNaoDaGrade)) {
				String aux = idNaoDaGrade;
				idNaoDaGrade = idDaGrade;
				idDaGrade = aux;
			}

			// TODO há um erro abaixo. Se já tem 2 disciplinas, tem que ver qual
			// é a obrigatória/eletiva, caso contrário se apareceu 2x a
			// equivalência, então dá erro. Ex: MAT114->MAT157 e MAT114->MAT156
			else if (ClassFactory.contains(_course, _curriculumId, idDaGrade)
					&& ClassFactory.contains(_course, _curriculumId, idNaoDaGrade))
				// throw new
				// IOException("Equivalência de duas disciplinas já existentes na grade: "
				// + idDaGrade + " <-> " + idNaoDaGrade);
				System.out.println("Equivalência de duas disciplinas já existentes na grade " + _curriculumId +": "
						+ idDaGrade + " <-> " + idNaoDaGrade);

			else if (!ClassFactory.contains(_course, _curriculumId, idDaGrade)
					&& !ClassFactory.contains(_course, _curriculumId, idNaoDaGrade))
				System.out.println("Equivalência de duas disciplinas não existentes na grade: "+ _curriculumId +" : "
						+ idDaGrade + " <-> " + idNaoDaGrade);

			c = ClassFactory.getClass(_course, _curriculumId, idDaGrade);
			Class c2 = ClassFactory.getClass(_course, _curriculumId, idNaoDaGrade);
			ClassFactory.addClass(_course, _curriculumId, idNaoDaGrade, c);
			
			c2 = ClassFactory.getClass(_course, _curriculumId, idNaoDaGrade);
			
		}

		in.close();
	}

	public String getCourse() {
		return _course;
	}

	public void setCourse(String course) {
		this._course = course;
	}

	public String getCurriculumId() {
		return _curriculumId;
	}

	public void setCurriculumId(String curriculumId) {
		this._curriculumId = curriculumId;
	}
}
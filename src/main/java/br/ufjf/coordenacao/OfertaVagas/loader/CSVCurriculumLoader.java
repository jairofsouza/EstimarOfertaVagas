package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassContainer;
import br.ufjf.coordenacao.OfertaVagas.model.ClassFactory;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;

public class CSVCurriculumLoader implements ICurriculumLoader {

	private File _mandatoryFile;
	private File _electiveFile;
	private File _equivalenceFile;
	private File _corequisiteFile;
	private Curriculum _cur;
	private boolean _multiple;
	private IFilter _filter;
	

	public CSVCurriculumLoader(File mandatoryFile, File electiveFile,File equivalenceFile, IFilter filter) {
		this(mandatoryFile, electiveFile, equivalenceFile, null, filter);

	}

	public CSVCurriculumLoader(File mandatoryFile, File electiveFile,File equivalenceFile) {
		this(mandatoryFile, electiveFile, equivalenceFile, new NoFilter());
	}
	
	public CSVCurriculumLoader(File mandatoryFile, File electiveFile,File equivalenceFile, File corequisiteFile, IFilter filter)
	{
		this._mandatoryFile = mandatoryFile;
		this._electiveFile = electiveFile;
		this._equivalenceFile = equivalenceFile;
		this._corequisiteFile = corequisiteFile;
		this._filter = filter;
		
		this._multiple = false;
		this._cur = new Curriculum();
	}
	
	public CSVCurriculumLoader(File mandatoryFile, File electiveFile,File equivalenceFile, File corequisiteFile)
	{
		this(mandatoryFile, electiveFile, equivalenceFile, corequisiteFile, new NoFilter());
	}

	private Curriculum processCurriculum(boolean multiple) {
		try {
			this._multiple = multiple;

			loadMandatoryFile();

			if (this._electiveFile != null)
				loadElectiveFile();

			//  necessrio que o processamento de equivalncias seja feito ao
			// final da carga de eletivas + obrigatrias
			if (this._equivalenceFile != null)
				loadEquivalenceFile();
			
			if(this._corequisiteFile != null)
				loadCorequisite();
		}

		catch (Exception e) {
			e.printStackTrace();
			//System.exit(1);
		}
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

			Class c = ClassFactory.getClass(_class);
			this._cur.addMandatoryClass(Integer.valueOf(semester), c);

			for (int i = 2; i < record.size()-1; i++) {
				String prerequisite = record.get(i).trim(); // Pr-requisito
				Class pre = ClassFactory.getClass(prerequisite);
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
			
			Class d = ClassFactory.getClass(disciplina);
			Class cr = ClassFactory.getClass(corequisito);
		
			d.addCorequisite(cr);
		}
		
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
		in.close();
	}
	
	private void loadElectiveFile() throws IOException {
		Reader in = new FileReader(this._electiveFile);
		Iterable<CSVRecord> electiveRecords = CSVFormat.EXCEL.parse(in);

		// TODO implementar o filtro
		for (CSVRecord record : electiveRecords) {
			if (this._filter.check(record)) {
				Class c = ClassFactory.getClass(record.get(0).trim());
				this._cur.addElectiveClass(c);

				for (int i = 1; i < record.size()-1; i++) {
					String prerequisite = record.get(i).trim(); // Pr-requisito
					Class pre = ClassFactory.getClass(prerequisite);
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
			if (!ClassFactory.contains(idDaGrade)
					&& ClassFactory.contains(idNaoDaGrade)) {
				String aux = idNaoDaGrade;
				idNaoDaGrade = idDaGrade;
				idDaGrade = aux;
			}

			// TODO h um erro abaixo. Se j tem 2 disciplinas, tem que ver qual
			//  a obrigatria/eletiva, caso contrrio se apareceu 2x a
			// equivalncia, ento d erro. Ex: MAT114->MAT157 e MAT114->MAT156
			else if (ClassFactory.contains(idDaGrade)
					&& ClassFactory.contains(idNaoDaGrade))
				// throw new
				// IOException("Equivalncia de duas disciplinas j existentes na grade: "
				// + idDaGrade + " <-> " + idNaoDaGrade);
				System.out.println("Equivalncia de duas disciplinas j existentes na grade: "
						+ idDaGrade + " <-> " + idNaoDaGrade);

			else if (!ClassFactory.contains(idDaGrade)
					&& !ClassFactory.contains(idNaoDaGrade))
				System.out.println("Equivalncia de duas disciplinas no existentes na grade: "
						+ idDaGrade + " <-> " + idNaoDaGrade);

			c = ClassFactory.getClass(idDaGrade);
			ClassFactory.addClass(idNaoDaGrade, c);
		}

		in.close();
	}
}

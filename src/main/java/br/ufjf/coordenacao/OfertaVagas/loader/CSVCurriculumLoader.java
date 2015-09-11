package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassFactory;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;

public class CSVCurriculumLoader implements ICurriculumLoader {

	private File _mandatoryFile;
	private File _electiveFile;
	private File _equivalenceFile;
	private Curriculum _cur;
	private boolean _multiple;
	private IFilter _filter;

	public CSVCurriculumLoader(File mandatoryFile, File electiveFile,File equivalenceFile, IFilter filter) {
		this._filter = filter;
		this._mandatoryFile = mandatoryFile;
		this._electiveFile = electiveFile;
		this._equivalenceFile = equivalenceFile;
		this._multiple = false;
		this._cur = new Curriculum();

	}

	public CSVCurriculumLoader(File mandatoryFile, File electiveFile,File equivalenceFile) {
		this(mandatoryFile, electiveFile, equivalenceFile, new NoFilter());
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
				String prerequisite = record.get(i).trim(); // Pré-requisito
				Class pre = ClassFactory.getClass(prerequisite);
				c.addPrerequisite(pre);
			}
			
			c.setWorkload(Integer.valueOf(record.get(record.size()-1).trim()));


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
					String prerequisite = record.get(i).trim(); // Pré-requisito
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

			// TODO há um erro abaixo. Se já tem 2 disciplinas, tem que ver qual
			// é a obrigatória/eletiva, caso contrário se apareceu 2x a
			// equivalência, então dá erro. Ex: MAT114->MAT157 e MAT114->MAT156
			else if (ClassFactory.contains(idDaGrade)
					&& ClassFactory.contains(idNaoDaGrade))
				// throw new
				// IOException("Equivalência de duas disciplinas já existentes na grade: "
				// + idDaGrade + " <-> " + idNaoDaGrade);
				System.out.println("Equivalência de duas disciplinas já existentes na grade: "
						+ idDaGrade + " <-> " + idNaoDaGrade);

			else if (!ClassFactory.contains(idDaGrade)
					&& !ClassFactory.contains(idNaoDaGrade))
				System.out.println("Equivalência de duas disciplinas não existentes na grade: "
						+ idDaGrade + " <-> " + idNaoDaGrade);

			c = ClassFactory.getClass(idDaGrade);
			ClassFactory.addClass(idNaoDaGrade, c);
		}

		in.close();
	}
}
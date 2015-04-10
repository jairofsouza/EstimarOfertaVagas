package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map.Entry;

import br.ufjf.coordenacao.OfertaVagas.loader.CSVCurriculumLoader;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class EstimatorContainer {
	private List<Curriculum> _listCurriculum; //Guarda uma List com curriculum recebidos na costrucao
	private StudentsHistory _sh; //Guarda o S. History recebido na construcao
	private List<EstimativesResult> _er; //Lista de cada EstimativeResult gerado a partir de cada _listCurriculum
	private EstimativesResult _joinedER; //Resultado final de todos os EstimativesResult de _er com os resultados somados
	private HashMap<String, Estimative> listEstimative; //HashMap com indice String: CodDisciplina, e valor Estimative da disciplina

	public EstimatorContainer(List<Curriculum> c, StudentsHistory sh) {
		this._listCurriculum = c;
		this._sh = sh;
	}

	// Ira processar cada curriculo e gerar um resultado que sera guardado na
	// lista de estimativas
	private void process() throws IOException {
		for (Curriculum c : _listCurriculum) 
		{
			Estimator e = new Estimator(c, _sh);

			_er.add(e.populateData().process(0.9f, 0.6f, 0.7f, 0.8f, 0.5f));
		}
	}

	// Junta todos os estimativeResult(de _er) em um HashMap (listEstimative)
	private void joinResult() {
		for (EstimativesResult er : _er) {
			ArrayList<Estimative> estimative = er.getEstimatives();

			if (listEstimative.size() == 0) {
				// Se tiver vazio, adiciona todos sem verificar;

				for(Estimative e: estimative)
				{
					listEstimative.put(e.getClassId(), e);
				}
			} 
			else {
				for(Estimative e: estimative) {
					String ClassId = e.getClassId();

					if (listEstimative.containsKey(ClassId)) {
						Estimative existent = listEstimative.get(ClassId);

						int hasPrereq = existent.getQtdHasAllPrereq()
								+ e.getQtdHasAllPrereq();
						int qdtCanHaveAllPreq = existent.getQtdCanHaveAllPreq()
								+ e.getQtdCanHaveAllPreq();
						int isEnrolled = existent.getQtdEnrolled()
								+ e.getQtdEnrolled();
						int reprovG = existent.getQtdReprovedGrade()
								+ e.getQtdReprovedGrade();
						int reprovF = existent.getQtdReprovedFreq()
								+ e.getQtdReprovedFreq();

						listEstimative.remove(ClassId);

						listEstimative.put(ClassId, new Estimative(ClassId,
								hasPrereq, qdtCanHaveAllPreq, isEnrolled,
								reprovG, reprovF));
					} else
						listEstimative.put(ClassId, e);
				}
			}
		}

		//Apos adicionar todas as disciplinas e somar as que ja existem, os valores de listEstimatives serao adicionados em um EstimativesResul
		Iterator<Entry<String, Estimative>> it = listEstimative.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Estimative> entry = it.next();

			Estimative e = entry.getValue();
			_joinedER.addEstimative(e);
		}

	}

	public EstimativesResult getEstimatives() throws IOException{

		// TODO tratar a IOException quando processa os multiplos resultados
		//System.out.println("Ocorreu um erro. " + e.getMessage());

		System.out.println("Iniciando processo.");
		this.process();

		System.out.println("Juntando resultados...");
		this.joinResult();

		return _joinedER;

	}

}


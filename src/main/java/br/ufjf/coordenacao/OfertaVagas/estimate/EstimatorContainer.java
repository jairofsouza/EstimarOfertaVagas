package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;


import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class EstimatorContainer {
	private List<Curriculum> _listCurriculum; // Guarda uma List com curriculum
												// recebidos na costrucao
	private StudentsHistory _sh; // Guarda o S. History recebido na construcao
	private List<EstimativesResult> _er; // Lista de cada EstimativeResult
											// gerado a partir de cada
											// _listCurriculum
	private EstimativesResult _joinedER; // Resultado final de todos os
											// EstimativesResult de _er com os
											// resultados somados
	private HashMap<String, Estimative> listEstimative; // HashMap com indice
														// String:
														// CodDisciplina, e
														// valor Estimative da
														// disciplina
	
	private TreeSet<Class> _mandatories;
	private TreeSet<Class> _electives;
	private Curriculum _joinedCurriculum;
	
	public EstimatorContainer(List<Curriculum> c, StudentsHistory sh) {
		this._listCurriculum = c;
		this._sh = sh;
		this._er = new ArrayList<EstimativesResult>();
		this._joinedER = new EstimativesResult();
		this.listEstimative = new HashMap<String, Estimative>();
		
		this._mandatories = new TreeSet<Class>();
		this._electives = new TreeSet<Class>();
	}

	// Ira processar cada curriculo e gerar um resultado que sera guardado na
	// lista de estimativas
	public void process() throws IOException {
		for (Curriculum c : _listCurriculum) {
			Estimator estimator = new Estimator(c, _sh);

			EstimativesResult result = estimator.populateData().process(0.9f,
					0.6f, 0.7f, 0.8f, 0.5f);

			_er.add(result);
		}
	}

	// Junta todos os estimativeResult(de _er) em um HashMap (listEstimative)
	private void joinResult() {
		for (EstimativesResult er : _er) {
			ArrayList<Estimative> estimative = er.getEstimatives();

			/*
			 * if (listEstimative.size() == 0) { // Se tiver vazio, adiciona
			 * todos sem verificar;
			 * 
			 * for(Estimative e: estimative) {
			 * listEstimative.put(e.getClassId(), e); } }
			 */
			// else {
			/*
			 * Senao, verifica o que ja tem guardado. Se a estimativa que
			 * estiver sendo analisada ja existir, pega-se os dados que existem
			 * e soma com os atuais para gerar uma nova, que substituira a
			 * antiga
			 */
			for (Estimative e : estimative) 
			{
				String ClassId = e.getClassId();
				
				//Mostra informacao sobre a estimativa atual.
				System.out.println("[INFO] Processando " + ClassId);
				System.out.println("[INFO] Estimativas para " + e + " (verificando se ja existe)");
				
				if (listEstimative.containsKey(ClassId)) 
				{
					System.out.printf("[INFO] " + ClassId + " ja existe.");
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
								
					Estimative est = new Estimative(ClassId, hasPrereq, qdtCanHaveAllPreq,
							isEnrolled, reprovG, reprovF);

					// TODO FIX::Pesos definidos como estaticos

					est.setQtdHasAllPrereqWeight(0.9f);
					est.setQtdEnrolledWeight(0.6f);
					est.setQtdCanHaveAllPreqWeight(0.7f);
					est.setQtdReprovedFreqWeight(0.8f);
					est.setQtdReprovedGradeWeight(0.5f);

					listEstimative.put(ClassId, est);
					System.out.println(" Alterado " + e + " para" + listEstimative.get(ClassId));
					
					}
				 else {
					listEstimative.put(ClassId, e);
					System.out.println("[INFO] Adicionado " + listEstimative.get(ClassId));
					
				}
				// }
			}

			// Apos adicionar todas as disciplinas e somar as que ja existem, os
			// valores de listEstimatives serao adicionados em um
			// EstimativesResul
			Iterator<Entry<String, Estimative>> it = listEstimative.entrySet()
					.iterator();
			while (it.hasNext()) {
				Entry<String, Estimative> entry = it.next();

				Estimative e = entry.getValue();
				_joinedER.addEstimative(e);
			}
		}
		}

	public void joinCurriculum() {
		for(Curriculum c: this._listCurriculum)
		{
			TreeSet<Class> mandatories = c.getMandatories().get(1);
			TreeSet<Class> electives = c.getElectives();
			
			for(Class cl: mandatories)
			{
				if(!this._mandatories.contains(cl))
					this._mandatories.add(cl);
				
				if(this._electives.contains(cl))
					this._electives.remove(cl);
			}
			
			for(Class cl: electives)
			{
				if(!this._mandatories.contains(cl) && !this._electives.contains(cl))
					this._electives.add(cl);
			}
						
		}
		
		this._joinedCurriculum = new Curriculum(this._mandatories, this._electives);
	}
	public EstimativesResult getEstimatives() throws IOException{

		// TODO tratar a IOException quando processa os multiplos resultados
		//System.out.println("Ocorreu um erro. " + e.getMessage());

		System.out.println("Iniciando processo.");
		this.process();

		System.out.println("Juntando resultados...");
		this.joinResult();
		this.joinCurriculum();
		
		return _joinedER;
	}
	
	public Curriculum getCurriculum()
	{
		if(this._joinedCurriculum == null)
			joinCurriculum();
		
		return this._joinedCurriculum;
	}

}


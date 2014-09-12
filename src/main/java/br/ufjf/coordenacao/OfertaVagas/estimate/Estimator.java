package br.ufjf.coordenacao.OfertaVagas.estimate;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;

public class Estimator {
	
	private Curriculum curriculum;
	private StudentsHistory history;
	private EstimativesResult result;

	public Estimator(Curriculum c, StudentsHistory sh) {
			this.curriculum = c;
			this.history = sh;
	}
	
/**
 * 
 * 		//Passos:
		// para cada disciplina da grade,
		    // pegar quais os seus pré-requisitos
		      // verificar quantos alunos já fizeram os pré-requisitos
		      // verificar quantos alunos estào fazendo os prerequisitos (1 ou mais)
		      
		     // para cada aluno, verificar se ele já cursou a disciplina (o aluno pode ter feito a disciplina mas não o seu pré-requisito)
		       // descontar do numero total
		
		    // permitir uma taxa de reprovação. Ou seja, daqueles que estao cursando, quantos podem se reprovar.

			// o aluno pode estar cursando a disciplina neste semestre...
 * 
 * 	
 */
		
	public EstimativesResult populateData() {

		// Fazendo somente com disciplinas obrigatórias
		this.result = new EstimativesResult();
		HashMap<Integer, TreeSet<Class>> mantadories = this.curriculum.getMandatories();
		Collection<Student> students = this.history.getStudents().values();
		for (Integer semester : mantadories.keySet()) {
			for (Class _class : mantadories.get(semester)) {
				int countCanEnroll = 0;
				int countIsEnrolled = 0;
				int countCompletePrereq = 0;
				
				for(Student st : students) {
					
					// Se o aluno já passou na disciplina, não entra na contagem
					if (st.getClasses(ClassStatus.APPROVED).contains(_class))
						continue;
					
					// Se o aluno está fazendo a disciplina, não precisa checar pré-requisitos
					if (st.getClasses(ClassStatus.ENROLLED).contains(_class))
						countIsEnrolled++;
					
					// Se o aluno não fez ou não está fazendo a disciplina, talvez ele possa fazer no próximo período
					else {
						int qtdPrereqCompleted = 0;
						int qtdPrereqEnrolled = 0;
						for(Class pre : _class.getPrerequisite()) {
							if (st.getClasses(ClassStatus.APPROVED).contains(pre)) 
								qtdPrereqCompleted++;
							
							if (st.getClasses(ClassStatus.ENROLLED).contains(pre)) 
								qtdPrereqEnrolled++;
						}
						
						if(qtdPrereqCompleted == _class.getPrerequisite().size()) countCompletePrereq++;
						else if (qtdPrereqCompleted + qtdPrereqEnrolled == _class.getPrerequisite().size())	countCanEnroll++;
					}
					
				}
				result.addEstimative(_class, countCompletePrereq, countCanEnroll, countIsEnrolled);
			}
		}

		return this.result;	
	}
	
	
}

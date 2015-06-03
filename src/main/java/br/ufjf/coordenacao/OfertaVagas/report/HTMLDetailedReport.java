package br.ufjf.coordenacao.OfertaVagas.report;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import br.ufjf.coordenacao.OfertaVagas.estimate.Estimative;
import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;
import br.ufjf.coordenacao.OfertaVagas.estimate.Estimator;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.model.Class;

public class HTMLDetailedReport extends EstimativeReport {

	private HashMap<ClassStatus, String> color = new HashMap<ClassStatus, String>();

	
	public HTMLDetailedReport(PrintStream out) {
		super(out);
		
		color.put(ClassStatus.APPROVED, "#0B2161");
		color.put(ClassStatus.ENROLLED, "#7777FF");
		color.put(ClassStatus.CAN_ENROLL, "#04B431");
		color.put(ClassStatus.PERHAPS_CAN_ENROLL_NEXT_SEMESTER, "#00FFBF");
		color.put(ClassStatus.REPROVED_FREQUENCY, "red");
		color.put(ClassStatus.REPROVED_GRADE, "orange");
	}
	
	
	public void generate(EstimativesResult result, StudentsHistory sh, Curriculum cur) throws IOException {
		
		printHTMLHeader();
		out.println("<body><table border=1>");
		ArrayList<Class> classes = printTableHeader(cur);
		printTableBody(classes, sh);
		printTableFooter(classes, result, sh.getStudents().keySet().size());
		printTableHeader(cur); // repetindo o cabeçalho no rodapé para facilitar a leitura da tabela...
		out.println("</table></body>");
		out.print("</html>");
	}
	
	//TODO é necessário criar uma solução que não necessite fazer vários FOR duplos
	private void printTableFooter(ArrayList<Class> classes, EstimativesResult result, int total) {

		out.print("<tr><td colspan=1000><b>Total de alunos matriculados: "+total+"</b></td></tr>");
		
		out.print("<tr><td colspan=1000><b>Abaixo, necessidade bruta de vagas </b>(considerando que todos os matriculados irao se reprovar e que todos os que podem cursar a disciplina irao pedir vaga)</td></tr>");

		//Imprimindo quantos matriculados em cada disciplina
		printTableFooterHelper(classes, result, "Matriculados");
		printTableFooterHelper(classes, result, "Repr. nota");
		printTableFooterHelper(classes, result, "Repr. freq");
		printTableFooterHelper(classes, result, "Habilitados");
		printTableFooterHelper(classes, result, "Quase habilit.");
		printTableFooterHelper(classes, result, "Necess. bruta");
		
		out.print("<tr><td colspan=1000><b>Abaixo, necessidade esperada de vagas </b>(% reprovacao: " + result.getEstimatives().get(0).getQtdEnrolledWeight() +
				"%; % alunos que podem cursar vao realmente cursar: " + result.getEstimatives().get(0).getQtdHasAllPrereqWeight() +
				"%; % alunos se aprovar nos prerequisitos: " + result.getEstimatives().get(0).getQtdCanHaveAllPreqWeight() +
				"%; % alunos com RI que vao rematricular: " + result.getEstimatives().get(0).getQtdReprovedFreqWeight() +
				"%; % alunos reprovados por nota que vao se rematricular: " + result.getEstimatives().get(0).getQtdReprovedGradeWeight() +
				"%.)</td></tr>");

		//Imprimindo quantos matriculados em cada disciplina
		printTableFooterHelper(classes, result, "%Matriculados");
		printTableFooterHelper(classes, result, "%Repr. nota");
		printTableFooterHelper(classes, result, "%Repr. freq");
		printTableFooterHelper(classes, result, "%Habilitados");
		printTableFooterHelper(classes, result, "%Quase habilit.");
		printTableFooterHelper(classes, result, "%Necess. bruta");
	}


	public void printTableFooterHelper(ArrayList<Class> classes, EstimativesResult result, String info) {
		
		String color = "white";
		if (info.equals("Matriculados")) color = this.color.get(ClassStatus.ENROLLED);
		else if (info.equals("Repr. nota")) color = this.color.get(ClassStatus.REPROVED_GRADE);
		else if (info.equals("Repr. freq")) color = this.color.get(ClassStatus.REPROVED_FREQUENCY);
		else if (info.equals("Habilitados")) color = this.color.get(ClassStatus.CAN_ENROLL);
		else if (info.equals("Quase habilit.")) color = this.color.get(ClassStatus.PERHAPS_CAN_ENROLL_NEXT_SEMESTER);
		else if (info.equals("%Matriculados")) color = this.color.get(ClassStatus.ENROLLED);
		else if (info.equals("%Repr. nota")) color = this.color.get(ClassStatus.REPROVED_GRADE);
		else if (info.equals("%Repr. freq")) color = this.color.get(ClassStatus.REPROVED_FREQUENCY);
		else if (info.equals("%Habilitados")) color = this.color.get(ClassStatus.CAN_ENROLL);
		else if (info.equals("%Quase habilit.")) color = this.color.get(ClassStatus.PERHAPS_CAN_ENROLL_NEXT_SEMESTER);
		
		out.print("<tr>");
		out.print("<td colspan=16><b>"+info+"<b></td>");
		for (Class c1 : classes) {
			for (Estimative estimative : result.getEstimatives()) {
				if (estimative.getClassId().equals(c1.getId())) {
					
					int valor = 0;
					if (info.equals("Matriculados")) valor = estimative.getQtdEnrolled();
					else if (info.equals("Repr. nota")) valor = estimative.getQtdReprovedGrade();
					else if (info.equals("Repr. freq")) valor = estimative.getQtdReprovedFreq();
					else if (info.equals("Habilitados")) valor = estimative.getQtdHasAllPrereq();
					else if (info.equals("Quase habilit.")) valor = estimative.getQtdCanHaveAllPreq();
					else if (info.equals("Necess. bruta")) valor = estimative.getQtdTotal();
					else if (info.equals("%Matriculados")) valor = estimative.getQtdEnrolledWeighted();
					else if (info.equals("%Repr. nota")) valor = estimative.getQtdReprovedGradeWeighted();
					else if (info.equals("%Repr. freq")) valor = estimative.getQtdReprovedFreqWeighted();
					else if (info.equals("%Habilitados")) valor = estimative.getQtdHasAllPrereqWeighted();
					else if (info.equals("%Quase habilit.")) valor = estimative.getQtdCanHaveAllPreqWeighted();
					else if (info.equals("%Necess. bruta")) valor = estimative.getQtdTotalWeighted();
					out.print("<td align=center style=\"background-color:"+color+"\">"+valor+"</td>");
				}
			}
		}
		out.print("<td colspan=60></td>");
		out.print("</tr>");
	}
	
	private void printTableBody(ArrayList<Class> classes, StudentsHistory sh) {
		
		ArrayList<String> student = new ArrayList<String>();
		student.addAll(sh.getStudents().keySet());
		Collections.sort(student);
		String colorCode = "";
		
		for (String stid : student) {
			out.print("<tr>");
			out.print("<td colspan=4>"+stid+"</td>");
			out.print("<td colspan=12>"+sh.getStudents().get(stid).getNome()+"</td>");
			Student st = sh.getStudents().get(stid);

			for (Class c1 : classes) {
			
				int retorno = Estimator.processStudentCourseStatus(c1, st);
				
				if (retorno == 0) 		colorCode = this.color.get(ClassStatus.APPROVED);
				else if (retorno == 1) 	colorCode = this.color.get(ClassStatus.ENROLLED);
				else if (retorno == 2)  colorCode = this.color.get(ClassStatus.REPROVED_GRADE);
				else if (retorno == 3)	colorCode = this.color.get(ClassStatus.REPROVED_FREQUENCY);
				else if (retorno == 4)	colorCode = this.color.get(ClassStatus.CAN_ENROLL);
				else if (retorno == 5)	colorCode = this.color.get(ClassStatus.PERHAPS_CAN_ENROLL_NEXT_SEMESTER);
				else colorCode = "white"; // Não pode cursar
				
				out.print("<td style=\"background-color:"+colorCode+"\"><div>&nbsp;");
				out.println("</div></td>");
			}
			
			printTableBodyOptional(st, classes);
			
			out.print("</tr>");
		}
	}
	
	/**
	 * Imprime todas as disciplinas que o aluno fez como opcionais
	 * @param st
	 * @param classes
	 */
	private void printTableBodyOptional(Student st, ArrayList<Class> classes) {
		//Retira as disciplinas que o aluno fez como obrigatórias e eletivas
		out.print("<td colspan=60>");

		String optional = "<b>Aprovado em </b>";
		for (Class class1 : st.getClasses(ClassStatus.APPROVED).keySet()) {
			if (!classes.contains(class1)) optional += class1.getId() + ",";
		}
		if (!optional.equals("<b>Aprovado em </b>"))
			out.print(optional.substring(0, optional.length()-1)+". ");

		optional = "<b>Matriculado em </b>";
		for (Class class1 : st.getClasses(ClassStatus.ENROLLED).keySet()) {
			if (!classes.contains(class1)) optional += class1.getId() + ",";
		}
		if (!optional.equals("<b>Matriculado em </b>"))
			out.print(optional.substring(0, optional.length()-1)+". ");

		out.print("</td>");
	}
	
	private void printHTMLHeader() {
		
		//CSS retirado daqui: http://stackoverflow.com/questions/6999523/rotate-text-as-headers-in-a-table-cross-browser
		out.println("<html>");
		out.println("<head>");
		out.println("<!--[if IE]> \n<style> .rotate_text { writing-mode: tb-rl; filter: flipH() flipV(); } </style>\n <![endif]--> ");
		out.println("<!--[if !IE]><!-->\n <style>");
		out.println(".rotate_text  { -moz-transform:rotate(-90deg); -moz-transform-origin: top left; -webkit-transform: rotate(-90deg);");
		out.println(" -webkit-transform-origin: top left; -o-transform: rotate(-90deg); -o-transform-origin: top left; position:relative;");
		out.println(" top:20px; }");
		out.println(" </style> \n<!--<![endif]-->");
		out.println(" <style>");
		out.println(" table { border: 1px solid black; table-layout: fixed; width: 50px; border-collapse: collapse; } ");
		out.println(" th, td { border: 1px solid black; width: 23px; font-family:Verdana; font-size:9pt; height: 20px}");
		out.println(" .rotated_cell { height:90px; vertical-align:bottom; font-family:Verdana; font-size:9pt;}");
		out.println("</style>");
		out.println("</head>");
	}
	
	private ArrayList<Class> printTableHeader(Curriculum c) {
		
		ArrayList<Integer> semester = new ArrayList<Integer>();
		semester.addAll(c.getMandatories().keySet());
		Collections.sort(semester);
		boolean pos = true;
		
		Class[] teste = { };
		ArrayList<Class> classes = new ArrayList<Class>();

		//4 celulas para a matrícula
		out.print("<tr><td></td><td></td><td></td><td></td>");

		//12 celulas para o nome
		out.print("<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>");
		
		//Imprimindo obrigatórias
		for (Integer sem : semester) {
			String color = pos ? "#A9E2F3" : "#CEECF5" ; 
			for (Class c1 : c.getMandatories().get(sem).toArray(teste)) {
				classes.add(c1);
				out.print("<td class=\"rotated_cell\" style=\"background-color:"+color+"\"><div class=\"rotate_text\">&nbsp;");
				out.print(c1.getId());
				out.println("</div></td>");
			}
			pos = !pos;
		}
		
		
		//Imprimindo eletivas
		String color = "white";
		for(Class c1 : c.getElectives()) {
			classes.add(c1);
			out.print("<td class=\"rotated_cell\" style=\"background-color:"+color+"\"><div class=\"rotate_text\">&nbsp;");
			out.print(c1.getId());
			out.println("</div></td>");			
		}
		
		// espaço para mostrar as opcionais (60 celulas)
		out.print("<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>");
		out.print("</tr>");
		
		return classes;
	}
		
}

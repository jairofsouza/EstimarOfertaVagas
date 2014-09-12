package br.ufjf.coordenacao.OfertaVagas.report;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import br.ufjf.coordenacao.OfertaVagas.estimate.Estimative;
import br.ufjf.coordenacao.OfertaVagas.estimate.EstimativesResult;

public class HTMLReport extends EstimativeReport {

	public HTMLReport(PrintStream out) {
		super(out);
	}
	
	public void generate(EstimativesResult result) throws IOException {
		ArrayList<Estimative> estimatives = result.getEstimatives();
		 
		
		//TODO fazer uma p‡gina HTML mais interessante
		out.print(printTopHtml());
		out.println("<tr><td>Class id</td><td> # has all prerequisites</td><td> # can complete all prerequisites</td><td> # enrolled</td><td> " +
				"# has all prerequisites and is expected to enroll</td><td> # expected to complete all reprequisites</td><td>" +
				"# expected to not complete the class</td><td> expected to enroll in the next semester</td></tr>");
		for (Estimative estimative : estimatives) {
			out.println("<tr font=\"Verdana\"><td>"+estimative.getClassId() + "</td><td>" 
					+ estimative.getQtdHasAllPrereq() + "</td><td>" 
					+ estimative.getQdtCanHaveAllPreq() + "</td><td>" 
					+ estimative.getQtdEnrolled() + "</td><td>"
					+ estimative.getQtdHasAllPrereqWeighted() + "</td><td>"
					+ estimative.getQdtCanHaveAllPreqWeighted() + "</td><td>"
					+ estimative.getQtdEnrolledWeighted() + "</td><td>"
					+estimative.getQtdTotal() + "</td><tr>");
		}
		out.print(printBottomHtml());
	}
	
	private String printTopHtml() {
		return "<html><body><table>";
	}
	
	private String printBottomHtml() {
		return "</table></body></html>";
	}
	
}

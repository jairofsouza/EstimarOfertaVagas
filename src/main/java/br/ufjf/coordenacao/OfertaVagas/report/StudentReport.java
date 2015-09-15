package br.ufjf.coordenacao.OfertaVagas.report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;

public class StudentReport {
	
	private PrintStream out;
	private HashMap<Class, ArrayList<String[]>> aprovado;
	
	public StudentReport(PrintStream out)
	{
		this.out = out;
	}

	public void generate(Student st, Curriculum cur)
	{
		printHTMLHeader();
		out.println("<body>");
		
		//Dados do aluno
		out.println("<table border=1 width=50% align=center>");
		out.println("<tr><td width=30% align=right>Matricula: </td><td align=center> " + st.getId() + "</td></tr>");
		out.println("<tr><td align=right>Aluno: </td><td align=center>" + st.getNome() + "</td></tr></table>" );
	
		//Disciplinas obrigatorias
		out.println("<br /><h3 align=center>Disciplinas Obrigatorias</h3>"
				+ "<table border=2 align=center width=50%><theader><tr><th width=10%>Periodo</th>"
				+ "<th width=60%>Disciplina</th><th>Situacao</theader><tbody>");
		
		aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED)); 
		TreeSet<String> naocompletado = new TreeSet<String>();
		
		boolean pos = true;
		for(int i: cur.getMandatories().keySet())
		{
			for(Class c: cur.getMandatories().get(i))
			{
				out.println("<tr");
				
				if(!aprovado.containsKey(c))
				{
					naocompletado.add(c.getId());
					out.println(" bgcolor=#FEFFBB><td align=center>"+ i + "</td><td align=center>" + c.getId() + "</td><td align=center>---!!!NAO APROVADO!!!---</td>");
				}
				else
				{
					String[] classdata = aprovado.get(c).get((aprovado.get(c).size() > 1 ? 1 : aprovado.get(c).size()-1));
					out.println(" bgcolor="+ (pos ? "#A9E2F3" : "#CEECF5") +"><td align=center>"+ i + "</td><td align=center> " + c.getId() + "</td><td align=center>APROVADO em " + classdata[0]+ "</td>");
					aprovado.remove(c);
					pos = !pos;
				}
				out.println("</tr>");
			}	
		}
		out.println("</tbody></table>");
		
		//Disciplinas Eletivas
		out.println("<br /><br /><h3 align=center>Eletivas Completadas</h3>");
		out.println("<table border=1 width=50% align=center><theader><tr><th>Disciplina</th><th>Situacao</th></tr></theader><tbody>");
		
		int creditos = 0;
		
		for(Class c: cur.getElectives())
		{
			if(aprovado.containsKey(c))
			{
				printTableRow(c);
				creditos += c.getWorkload();
				aprovado.remove(c);
			} 	
		}
		
		out.println("</tbody></tfoot><tr><td align=right width=100% colspan=2>Carga horaria em eletivas: " + creditos + "</td></tfoot></table>");
		
		//Disciplinas Opcionais
		out.println("<br /><br /><h3 align=center>Opcionais Completadas</h3>");
		out.println("<table border=1 width=50% align=center><theader><tr><th>Disciplina</th><th>Situacao</th></tr></theader><tbody>");
		creditos = 0;
		
		Set<Class> ap = aprovado.keySet();
		Iterator<Class> i = ap.iterator();
		while(i.hasNext())
		{
				Class c = i.next();
				printTableRow(c);
				creditos += c.getWorkload();
		}
		
		out.println("</tbody></tfoot><tr><td align=right width=100% colspan=2>Carga horaria em opcionas: " + creditos + "</td></tfoot></table>");
		
	}
	
	private void printHTMLHeader()
	{
		out.println("<html><head><style> table { border: 1px solid black; border-collapse: collapse; }</style></head>");
	}
	
	private void printTableRow(Class c)
	{
		String[] classdata = aprovado.get(c).get((aprovado.get(c).size() > 1 ? 1 : aprovado.get(c).size()-1));
		out.println("<tr><td align=center width=60%>" + c.getId() + "</td><td align=center>Concluido em " + classdata[0] + "</td></tr>");
	}
}

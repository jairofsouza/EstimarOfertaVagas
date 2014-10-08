package br.ufjf.coordenacao.OfertaVagas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Scanner;

import br.ufjf.ice.integra3.rs.restclient.RSCursoAlunosDiscSituacao;
import br.ufjf.ice.integra3.rs.restclient.model.AlunoCurso;
import br.ufjf.ice.integra3.rs.restclient.model.CursoAlunosSituacaoResponse;
import br.ufjf.ice.integra3.rs.restclient.model.Disciplina;
import br.ufjf.ice.integra3.ws.login.interfaces.IWsLogin;
import br.ufjf.ice.integra3.ws.login.interfaces.WsException_Exception;
import br.ufjf.ice.integra3.ws.login.interfaces.WsLoginResponse;
import br.ufjf.ice.integra3.ws.login.service.WSLogin;

public class GetStudentData {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		FileReader file = new FileReader("application.token");
		BufferedReader bf = new BufferedReader(file);
		
		String token = bf.readLine();
		Scanner sc = new Scanner(System.in);

		System.out.print("Código do curso: ");
		String codigo = sc.next();

		System.out.print("User: ");
		String login = sc.next();
		
		System.out.print("Password: ");
		String pwd = sc.next();
	    pwd = md5(pwd);
		
		try {
			System.out.println("Logando...");
			IWsLogin integra = new WSLogin().getWsLoginServicePort();
			WsLoginResponse user = integra.login(login, pwd, token);

//			WsUserInfoResponse infos = integra.getUserInformation(user.getToken()); // Pegando informações
//			System.out.println(infos.getCpf()); // Cpf
//			System.out.println(infos.getEmail()); // Email

			System.out.println("Recuperando dados do curso "+codigo+"...");
            RSCursoAlunosDiscSituacao rsClient = new RSCursoAlunosDiscSituacao(user.getToken());
            
            CursoAlunosSituacaoResponse rsResponse = rsClient.get(codigo);

            String filename = "data/dados_alunos_"+codigo+Calendar.getInstance().getTimeInMillis()+".csv";
            System.out.println("Criando o arquivo "+filename);
            FileWriter fileout = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fileout);
            for(AlunoCurso alunoCurso : rsResponse.getAluno()) {
            	for (Disciplina disciplina : alunoCurso.getDisciplinas().getDisciplina()) {
	                bw.write(alunoCurso.getCurso());
	                bw.write(";"+alunoCurso.getMatricula());
	                bw.write(";"+alunoCurso.getNome());
	                bw.write(";"+alunoCurso.getCurriculo());
	                bw.write(";"+disciplina.getAnoSemestre());
	                bw.write(";"+disciplina.getDisciplina());
	                bw.write(";"+(disciplina.getNota()==null ? "" : disciplina.getNota().trim()));
	                bw.write(";"+disciplina.getSituacao() + "\n");
            	}
            }
    		bw.close();
    		fileout.close();
            System.out.println("Finalizado");
			
			
		} catch (WsException_Exception e) {
			//Impressão de erros
			System.out.println(e.getMessage());
			System.out.println(e.getFaultInfo().getErrorUserMessage());
		}

		bf.close();
		file.close();
		
	}
	
	   public static String md5(String input) {
	         
	        String md5 = null;
	         
	        if(null == input) return null;
	         
	        try {
		        //Create MessageDigest object for MD5
		        MessageDigest digest = MessageDigest.getInstance("MD5");
		         
		        //Update input string in message digest
		        digest.update(input.getBytes(), 0, input.length());
		 
		        //Converts message digest value in base 16 (hex) 
		        md5 = new BigInteger(1, digest.digest()).toString(16);
	 
	        } catch (NoSuchAlgorithmException e) {
	 
	            e.printStackTrace();
	        }
	        return md5;
	    }

}

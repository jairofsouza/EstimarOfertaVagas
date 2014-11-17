package br.ufjf.ice.integra3.rs.restclient.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Augusto
 */
@XmlRootElement
public class CursoAlunosSituacaoResponse implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8701304217730511286L;
	private List<AlunoCurso> alunosList;
	private String responseStatus;
    
    public CursoAlunosSituacaoResponse()
    {
        
    }
    
    public CursoAlunosSituacaoResponse(List<AlunoCurso> alunosCursoList, String responseStatus)
    {
        this.alunosList = alunosCursoList;
        this.responseStatus = responseStatus;
    }

    public String getResponseStatus() {
		return responseStatus;
	}
    
    public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
    
    /**
     * @return the alunosList
     */
    public List<AlunoCurso> getAluno() {
        return alunosList;
    }

    /**
     * @param alunosList the alunosList to set
     */
    public void setAluno(List<AlunoCurso> alunosList) {
        this.alunosList = alunosList;
    }
}

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
    
    public CursoAlunosSituacaoResponse()
    {
        
    }
    
    public CursoAlunosSituacaoResponse(List<AlunoCurso> alunosCursoList)
    {
        this.alunosList = alunosCursoList;
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

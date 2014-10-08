/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufjf.ice.integra3.rs.restclient.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Augusto
 */
@XmlRootElement
public class Disciplina implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8783294053599926336L;
	private String disciplina;
    private String anoSemestre;
    private String nota;
    private String situacao;
    
    public Disciplina()
    {
        
    }
    
    public Disciplina(String disciplina, String anoSemestre, String nota, String situacao)
    {
        this.disciplina = disciplina;
        this.anoSemestre = anoSemestre;
        this.nota = nota;
        this.situacao = situacao;
    }

    /**
     * @return the disciplina
     */
    public String getDisciplina() {
        return disciplina;
    }

    /**
     * @param disciplina the disciplina to set
     */
    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    /**
     * @return the anoSemestre
     */
    public String getAnoSemestre() {
        return anoSemestre;
    }

    /**
     * @param anoSemestre the anoSemestre to set
     */
    public void setAnoSemestre(String anoSemestre) {
        this.anoSemestre = anoSemestre;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @param nota the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}

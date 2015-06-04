package br.ufjf.ice.integra3.rs.restclient;

import br.ufjf.ice.integra3.rs.restclient.model.CursoAlunosSituacaoResponse;
import org.apache.cxf.jaxrs.client.WebClient;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Luis Augusto
 */
public class RSCursoAlunosDiscSituacao 
{
    // URL do serviço em Rest
    private static final String BASE_URL = "http://integra.ufjf.br/integra/services/rs/alunos/curso/situacao/v1/getcursoalunosdiscsituacao";
    
    private WebClient rsClient;
    
    public RSCursoAlunosDiscSituacao(String authToken)
    {
        this.rsClient = WebClient.create(BASE_URL);
        this.rsClient.header("IntegraToken", authToken); // Adiciono cabecalho de autenticação
    }
    
    public CursoAlunosSituacaoResponse get(String curso)
    {
        return this.rsClient.path(String.format("adm/%s", curso)).accept(MediaType.APPLICATION_XML).get(CursoAlunosSituacaoResponse.class);
    }
}

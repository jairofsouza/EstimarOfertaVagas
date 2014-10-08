# EstimarOfertaVagas

Projeto para estimar oferta de vagas para os cursos de graduação da Universidade Federal de Juiz de Fora


## Para instalar

instalar maven3 e executar mvn install no diretório raiz do projeto.


## Para executar

Primeiro é necessário baixar o histórico de todos os alunos através da classe br.ufjf.coordenacao.OfertaVagas.GetStudentData. Para rodar essa classe, é necessário ter o token de acesso ao web service. Com o token em mãos, crie um arquivo application.token no root do projeto com o token fornecido. A classe br.ufjf.coordenacao.OfertaVagas.GetStudentData não necessita de parâmetros.


A classe br.ufjf.coordenacao.OfertaVagas.ProcessData é a principal do projeto. No método main() há um exemplo de chamada.
São necessários os seguintes inputs:
* arquivo csv com o histórico de todos os alunos do curso [obrigatório]
* arquivo csv com a grade de disciplinas obrigatórias [obrigatório]
* arquivo csv com a grade de disciplinas eletivas [opcional]
* arquivo csv com as equivalências de disciplinas [opcional]

#### Observação

* no diretório data estão arquivos de exemplo com a grade do curso de sistema de informação
* coordenadores têm direito de baixar os dados de alunos somente do curso que coordena. Chefes de departamento podem baixar de alunos de todos os cursos que o departamento possui disciplina na grade.


EstimarOfertaVagas
==================

Projeto para estimar oferta de vagas para os cursos de graduação da Universidade Federal de Juiz de Fora


Para instalar
=============

instalar maven3 e executar mvn install no diretório raiz do projeto.


Para executar
=============

A classe br.ufjf.coordenacao.OfertaVagas.ProcessData é a principal do projeto. No método main() há um exemplo de chamada.
São necessárias os seguintes inputs:
* arquivo csv com o histórico de todos os alunos do curso [obrigatório]
* arquivo csv com a grade de disciplinas obrigatórias [obrigatório]
* arquivo csv com a grade de disciplinas eletivas [opcional]
* arquivo csv com as equivalências de disciplinas [opcional]

Observação: 
* no diretório data estão arquivos de exemplo com a grade do curso de sistema de informação
* o suporte do instituto tem gerado os arquivos csv com dados dos alunos. Contudo, já foi combinado que eles irão criar um serviço web e o sistema se encarregará de fazer a carga desses dados automaticamente.

package br.ufjf.coordenacao.OfertaVagas.loader;

import java.io.IOException;

import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;

public interface ICurriculumLoader {
	public Curriculum getCurriculum() throws IOException;

}

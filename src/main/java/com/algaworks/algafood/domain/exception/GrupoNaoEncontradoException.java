package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradoException extends NegocioException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GrupoNaoEncontradoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public GrupoNaoEncontradoException(Long grupoId) {
		this(String.format("Não existe cadastro de Grupo com o código %d", grupoId));
	}

}

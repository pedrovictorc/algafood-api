package com.algaworks.algafood.domain.exception;

public class EstadoNaoEncontradoException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public EstadoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public EstadoNaoEncontradoException(Long estadoId) {
		this(String.format("Não existe cadastro de Estado com o código %d", estadoId));
	}

}

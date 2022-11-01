package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException{

	public PedidoNaoEncontradoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public PedidoNaoEncontradoException(Long pedidoId) {
		this(String.format("Não existe cadastro de permissão com o código %d", pedidoId));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

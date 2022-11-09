package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException{

	public PedidoNaoEncontradoException(String codigoPedido) {
		super(String.format("Não existe cadastro de permissão com o código %s", codigoPedido));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

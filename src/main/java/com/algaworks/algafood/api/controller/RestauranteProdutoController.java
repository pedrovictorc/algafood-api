package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.disassembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {

	@Autowired 
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private CadastroProdutoService cadastroProduto;
	
	@Autowired
	private ProdutoModelAssembler produtoModelAssembler;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoInputDisassembler produtoInputDisassembler;

	@GetMapping
	public List<ProdutoModel> listar(@PathVariable Long restauranteId){
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		List<Produto> todosProdutos = produtoRepository.findByRestaurante(restaurante);
		
		return produtoModelAssembler.toCollectionModel(todosProdutos);
	}
	
	@GetMapping("/{produtoId}")
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		Produto produto = cadastroProduto.buscarOuFalhar(produtoId, restauranteId);
		
		return produtoModelAssembler.toModel(produto);
		
	}
	
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel adicionar(@PathVariable Long restauranteId,
            @RequestBody @Valid ProdutoInput produtoInput) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
        
        Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);
        
        produto = cadastroProduto.salvar(produto);
        
        return produtoModelAssembler.toModel(produto);
    }
	
	@PutMapping("/{produtoId}")
    public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
            @RequestBody @Valid ProdutoInput produtoInput) {
        Produto produtoAtual = cadastroProduto.buscarOuFalhar(produtoId, restauranteId);
        
        produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);
        
        produtoAtual = cadastroProduto.salvar(produtoAtual);
        
        return produtoModelAssembler.toModel(produtoAtual);
    }
	
//	@DeleteMapping("/{formaPagamentoId}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
//		cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);
//	}
//
//	@PutMapping("/{formaPagamentoId}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
//		cadastroRestaurante.associarFormaPagamento(restauranteId, formaPagamentoId);
//	}

}

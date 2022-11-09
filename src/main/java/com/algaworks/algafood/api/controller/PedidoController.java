package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.disassembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infrastructure.reposiroty.PedidoSpecs;
import com.google.common.collect.ImmutableMap;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {
	
	@Autowired
	private EmissaoPedidoService pedidoService;
	
	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModel;
	
	@Autowired
	private PedidoModelAssembler pedidoModel;
	
	@Autowired	
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;
	
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String campos){
//		List<Pedido> pedidos = pedidoRepository.findAll();
//		List<PedidoResumoModel> pedidosModel = pedidoResumoModel.toCollectioModel(pedidos);
//		
//		MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);
//		
//		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//		filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//		
//		if(StringUtils.isNotBlank(campos)) {
//			filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
//			
//		}
//		
//		pedidosWrapper.setFilters(filterProvider);
//		
//		return pedidosWrapper;
//	}
	
	
	@GetMapping
	public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable){
		pageable = traduzirPageable(pageable);
		Page<Pedido> pagePedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);
		
		List<PedidoResumoModel> pedidoModel = pedidoResumoModel.toCollectioModel(pagePedidos.getContent());
		
		Page<PedidoResumoModel> pedidoResumoPage = new PageImpl<>(pedidoModel, pageable, pagePedidos.getTotalElements());
		
		return pedidoResumoPage;
	}
	
	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		Pedido pedido = pedidoService.buscarOuFalhar(codigoPedido);
		return pedidoModel.toModel(pedido);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel salvar(@RequestBody @Valid PedidoInput pedidoInput) {
		try {
		Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);
		// TODO pegar usuário autenticado
		novoPedido.setCliente(new Usuario());
		novoPedido.getCliente().setId(1L);;
		
		novoPedido = pedidoService.emitir(novoPedido);
		
		return pedidoModel.toModel(novoPedido);
		}catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}
	
	
	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = ImmutableMap.of(
				"codigo","codigo",
				"subTotal","subTotal",
				"restaurante.nome","restaurante.nome",
				"cliente.nome", "cliente.nome",
				"valorTotal","valorTotal");
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}

}

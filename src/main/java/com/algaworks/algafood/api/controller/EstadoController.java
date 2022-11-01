package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping(value = "/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CadastroEstadoService cadastroEstado;


	@GetMapping
	public List<Estado> listar(){
		return estadoRepository.findAll();
	}
	
	@GetMapping(value = "/{estadoId}")
	public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {
		Optional <Estado> estado = estadoRepository.findById(estadoId);
		if (estado.isPresent()) {
			return ResponseEntity.ok(estado.get());
		}
		return ResponseEntity.notFound().build();

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> adicionar(@RequestBody Estado estado){
		try {
			estado = cadastroEstado.salvar(estado);

			return ResponseEntity.status(HttpStatus.CREATED).body(estado);
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}


	@PutMapping(value = "/{estadoId}")
	public ResponseEntity<?> atualizar(@PathVariable Long estadoId, @RequestBody Estado estado){
		Optional <Estado> estadoAtual = estadoRepository.findById(estadoId);

		try {
			if (estadoAtual.isPresent() ) {
				BeanUtils.copyProperties(estado, estadoAtual.get(), "id");

				Estado estadoSalvo = cadastroEstado.salvar(estadoAtual.get());

				return ResponseEntity.ok(estadoSalvo);
			}
			return ResponseEntity.notFound().build();
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping(value = "/{estadoId}")
	public ResponseEntity<Estado> excluir(@PathVariable Long estadoId){
		try {
			cadastroEstado.excluir(estadoId);
			
			return ResponseEntity.noContent().build();
			
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		}catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

}

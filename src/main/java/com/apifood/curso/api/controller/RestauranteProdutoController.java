package com.apifood.curso.api.controller;

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

import com.apifood.curso.api.assembler.ProdutoInputDisassembler;
import com.apifood.curso.api.assembler.ProdutoModelAssembler;
import com.apifood.curso.api.model.ProdutoModel;
import com.apifood.curso.api.model.input.ProdutoInput;
import com.apifood.curso.domain.model.Produto;
import com.apifood.curso.domain.model.Restaurante;
import com.apifood.curso.domain.repository.ProdutoRepository;
import com.apifood.curso.domain.service.CadastroProdutoService;
import com.apifood.curso.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private CadastroProdutoService cadastroProduto;
	
	@Autowired
	private ProdutoModelAssembler produtoAssembler;
	
	@Autowired
	private ProdutoInputDisassembler produtoDisassembler;
	
	@GetMapping
	public List<ProdutoModel> listar(@PathVariable Long restauranteId){
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		List<Produto> todosProdutos = produtoRepository.findByRestaurante(restaurante);
		
		return produtoAssembler.toCollectionModel(todosProdutos);
	}
	
	@GetMapping("/{produtoId}")
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);
		
		return produtoAssembler.toModel(produto);
	}
	
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel adicionar(@PathVariable Long restauranteId,
            @RequestBody @Valid ProdutoInput produtoInput) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
        
        Produto produto = produtoDisassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);
        
        produto = cadastroProduto.salvar(produto);
        
        return produtoAssembler.toModel(produto);
    }
	
	@PutMapping("/{produtoId}")
    public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
            @RequestBody @Valid ProdutoInput produtoInput) {
        Produto produtoAtual = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);
        
        produtoDisassembler.copyToDomainObject(produtoAtual, produtoInput);
        
        produtoAtual = cadastroProduto.salvar(produtoAtual);
        
        return produtoAssembler.toModel(produtoAtual);
    }   
	
	
}

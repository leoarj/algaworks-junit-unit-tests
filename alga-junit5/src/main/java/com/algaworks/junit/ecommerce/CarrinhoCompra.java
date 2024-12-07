package com.algaworks.junit.ecommerce;

import java.math.BigDecimal;
import java.util.*;

public class CarrinhoCompra {

	private final Cliente cliente;
	private final List<ItemCarrinhoCompra> itens;

	public CarrinhoCompra(Cliente cliente) {
		this(cliente, new ArrayList<>());
	}

	public CarrinhoCompra(Cliente cliente, List<ItemCarrinhoCompra> itens) {
		Objects.requireNonNull(cliente);
		Objects.requireNonNull(itens);
		this.cliente = cliente;
		this.itens = new ArrayList<>(itens); //Cria lista caso passem uma imutável
	}

	// Aqui mudei para retorar uma lista modificável mesmo,
	// mas antes estava utilizando Collections.unmodifiableList(this.itens).
	public List<ItemCarrinhoCompra> getItens() {
		//DONE deve retornar uma nova lista para que a antiga não seja alterada
		//return Collections.unmodifiableList(this.itens);
		return new ArrayList<>(this.itens);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void adicionarProduto(Produto produto, int quantidade) {
		//DONE parâmetros não podem ser nulos, deve retornar uma exception
		validarProduto(produto);
		//DONE quantidade não pode ser menor que 1
		if (quantidade < 1) {
			throw new IllegalArgumentException("Quantidade do produto deve ser maior ou igual a 1");
		}
		//DONE deve incrementar a quantidade caso o produto já exista
		Optional<ItemCarrinhoCompra> optionalItemCarrinhoCompra = buscarItemPeloProduto(produto);

        optionalItemCarrinhoCompra.ifPresentOrElse(
				itemCarrinhoCompra -> itemCarrinhoCompra.adicionarQuantidade(quantidade),
				() -> this.itens.add(new ItemCarrinhoCompra(produto, quantidade)));
	}

	public void removerProduto(Produto produto) {
		//DONE parâmetro não pode ser nulo, deve retornar uma exception
		validarProduto(produto);
		//DONE caso o produto não exista, deve retornar uma exception
		ItemCarrinhoCompra optionalItemCarrinhoCompra = buscarItemPeloProdutoOuFalhar(produto);
		//DONE deve remover o produto independente da quantidade
		this.itens.remove(optionalItemCarrinhoCompra);
	}

	public void aumentarQuantidadeProduto(Produto produto) {
		//DONE parâmetro não pode ser nulo, deve retornar uma exception
		validarProduto(produto);
		//DONE caso o produto não exista, deve retornar uma exception
		ItemCarrinhoCompra itemCarrinhoCompra = buscarItemPeloProdutoOuFalhar(produto);
		//DONE deve aumentar em um quantidade do produto
		itemCarrinhoCompra.adicionarQuantidade(1);
	}

    public void diminuirQuantidadeProduto(Produto produto) {
		//DONE parâmetro não pode ser nulo, deve retornar uma exception
		validarProduto(produto);
		//DONE caso o produto não exista, deve retornar uma exception
		ItemCarrinhoCompra itemCarrinhoCompra = buscarItemPeloProdutoOuFalhar(produto);
		//DONE deve diminuir em um quantidade do produto, caso tenha apenas um produto, deve remover da lista
		if (itemCarrinhoCompra.getQuantidade() > 1) {
			itemCarrinhoCompra.subtrairQuantidade(1);
		} else {
			this.itens.remove(itemCarrinhoCompra);
		}
	}

    public BigDecimal getValorTotal() {
		//DONE implementar soma do valor total de todos itens
		return this.itens.stream()
				.map(ItemCarrinhoCompra::getValorTotal)
				.reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

	public int getQuantidadeTotalDeProdutos() {
		//DONE retorna quantidade total de itens no carrinho
		//DONE Exemplo em um carrinho com 2 itens, com a quantidade 2 e 3 para cada item respectivamente, deve retornar 5
		return this.itens.stream()
				.mapToInt(ItemCarrinhoCompra::getQuantidade)
				.sum();
	}

	public void esvaziar() {
		//DONE deve remover todos os itens
		this.itens.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CarrinhoCompra that = (CarrinhoCompra) o;
		return Objects.equals(itens, that.itens) && Objects.equals(cliente, that.cliente);
	}

	@Override
	public int hashCode() {
		return Objects.hash(itens, cliente);
	}

	private void validarProduto(Produto produto) {
		if (Objects.isNull(produto)) {
			throw new IllegalArgumentException("Produto não pode ser nulo");
		}
	}

	/*
	* Métodos de conveniência adicionados
	* Devem também ter testes?
	* */
	public Optional<ItemCarrinhoCompra> buscarItemPeloProduto(Produto produto) {
		return itens.stream()
				.filter(item -> item.getProduto().equals(produto))
				.findFirst();
	}

	public ItemCarrinhoCompra buscarItemPeloProdutoOuFalhar(Produto produto) {
		return buscarItemPeloProduto(produto)
				.orElseThrow(() -> new RuntimeException("Produto inexistente"));
	}

	public boolean produtoExisteNoCarrinho(Produto produto) {
		return itens.stream()
				.anyMatch(item -> item.getProduto().equals(produto));
	}

	public boolean produtoNaoExisteNoCarrinho(Produto produto) {
		return !produtoExisteNoCarrinho(produto);
	}
}
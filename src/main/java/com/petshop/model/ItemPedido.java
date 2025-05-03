package com.petshop.model;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_de_pedidos")
public class ItemPedido {

    // ID Próprio - NECESSÁRIO para JPA mapear como entidade, mas não está no schema
    // original.
    // Assumindo que a tabela pode ser alterada para incluir um ID ou que uma chave
    // composta seria usada (@EmbeddedId)
    // A abordagem mais simples compatível com o controller é adicionar um ID.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Usando Integer aqui, pois o controller usa Integer itemId

    // --- Foreign Keys mapeadas como ManyToOne ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_pedidos_numero_pedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_vendedores_id")
    private Vendedor vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_produtos_id")
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_clientes_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_animais_id", nullable = true) // Permitir nulo se o item não for para um animal específico
    private Animal animal;

    // Atributos adicionais que geralmente existem em itens de pedido
    // (Não estão na tabela itens_da_venda, mas são necessários para a lógica)
    // Se não puder adicionar colunas, estes valores teriam que ser
    // calculados/buscados de outra forma
    @Column(name = "quantidade")
    private Integer quantidade = 1;

    @Column(name = "preco_unitario")
    private Double precoUnitario;

    // --- Construtores
    public ItemPedido() {
    }

    public ItemPedido(Integer id, Pedido pedido, Vendedor vendedor, Produto produto, Cliente cliente, Animal animal,
            Integer quantidade, Double precoUnitario) {
        this.id = id;
        this.pedido = pedido;
        this.vendedor = vendedor;
        this.produto = produto;
        this.cliente = cliente;
        this.animal = animal;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters, Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    // Método utilitário para calcular subtotal
    public Double getSubtotal() {
        if (precoUnitario != null && quantidade != null) {
            return precoUnitario * quantidade;
        }
        return 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id); // Baseado no ID gerado
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Baseado no ID gerado
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", produtoId=" + (produto != null ? produto.getId() : "null") +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                '}';
    }
}
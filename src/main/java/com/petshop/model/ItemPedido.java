package com.petshop.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


import jakarta.persistence.*;

@Entity
@Table(name = "itens_de_pedidos")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "fk_pedidos_numero_pedido", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "fk_produtos_id", nullable = false)
    private Produto produto;
    
    @ManyToOne
    @JoinColumn(name = "fk_vendedores_id", nullable = false)
    private Vendedor vendedor;
    
    @ManyToOne
    @JoinColumn(name = "fk_clientes_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "fk_animais_id")
    private Animal animal;
    
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;
    
    @Column(name = "preco_unitario", nullable = false)
    private Double precoUnitario;
    
    @Transient
    private Double subtotal;
    
    // Construtores
    public ItemPedido() {
        this.quantidade = 1;
    }
    
    public ItemPedido(Pedido pedido, Produto produto, Vendedor vendedor, Cliente cliente, Animal animal, Integer quantidade, Double precoUnitario) {
        this.pedido = pedido;
        this.produto = produto;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.animal = animal;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Pedido getPedido() {
        return pedido;
    }
    
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    public Produto getProduto() {
        return produto;
    }
    
    public void setProduto(Produto produto) {
        this.produto = produto;
        if (produto != null && this.precoUnitario == null) {
            this.precoUnitario = produto.getPreco();
        }
    }
    
    public Vendedor getVendedor() {
        return vendedor;
    }
    
    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
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
    
    public Double getSubtotal() {
        if (quantidade != null && precoUnitario != null) {
            return quantidade * precoUnitario;
        }
        return 0.0;
    }
    
    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ItemPedido that = (ItemPedido) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    // ToString
    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", produto=" + (produto != null ? produto.getNome() : "null") +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                '}';
    }


}
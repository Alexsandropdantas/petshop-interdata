package com.petshop.model;


import jakarta.persistence.*;

@Entity
@Table(name = "itens_de_pedidos")
public class ItemDePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "preco_unitario")
    private Double precoUnitario;

    @Column(name = "desconto") // Desconto específico do item
    private Double desconto; // Armazenar como percentual (ex: 10.0 para 10%) ou valor? Usaremos percentual.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_pedidos_numero_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_produtos_id", nullable = false)
    private Produto produto;

    // As FKs para cliente, animal e vendedor estão na tabela de itens.
    // Isso sugere que cada ITEM pode ter um cliente/animal/vendedor diferente,
    // o que é incomum para um pedido. Geralmente, o pedido tem UM cliente e UM vendedor.
    // Se for esse o caso, mova essas FKs para a entidade Pedido.
    // Vou manter como está no schema, mas considere a lógica de negócio.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_clientes_id")
    private Cliente cliente; // Cliente associado a este item específico

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_animais_id")
    private Animal animal; // Animal associado a este item específico

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_vendedores_id")
    private Vendedor vendedor; // Vendedor associado a este item específico

    //Construtores
    public ItemDePedido() {
    }

    public ItemDePedido(Integer id, Produto produto, Integer quantidade,  Double desconto, Pedido pedido,
             Cliente cliente, Animal animal, Vendedor vendedor) {
        this.id = id;
        this.quantidade = quantidade;
        this.desconto = desconto;
        this.pedido = pedido;
        this.produto = produto;
        this.cliente = cliente;
        this.animal = animal;
        this.vendedor = vendedor;
    }

    
    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
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

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public String toString() {
        return "ItensDePedido [id=" + id + ", quantidade=" + quantidade + ", precoUnitario=" + precoUnitario
                + ", desconto=" + desconto + ", pedido=" + pedido + ", produto=" + produto + ", cliente=" + cliente
                + ", animal=" + animal + ", vendedor=" + vendedor + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
        result = prime * result + ((precoUnitario == null) ? 0 : precoUnitario.hashCode());
        result = prime * result + ((desconto == null) ? 0 : desconto.hashCode());
        result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
        result = prime * result + ((produto == null) ? 0 : produto.hashCode());
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        result = prime * result + ((animal == null) ? 0 : animal.hashCode());
        result = prime * result + ((vendedor == null) ? 0 : vendedor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemDePedido other = (ItemDePedido) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (quantidade == null) {
            if (other.quantidade != null)
                return false;
        } else if (!quantidade.equals(other.quantidade))
            return false;
        if (precoUnitario == null) {
            if (other.precoUnitario != null)
                return false;
        } else if (!precoUnitario.equals(other.precoUnitario))
            return false;
        if (desconto == null) {
            if (other.desconto != null)
                return false;
        } else if (!desconto.equals(other.desconto))
            return false;
        if (pedido == null) {
            if (other.pedido != null)
                return false;
        } else if (!pedido.equals(other.pedido))
            return false;
        if (produto == null) {
            if (other.produto != null)
                return false;
        } else if (!produto.equals(other.produto))
            return false;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        if (animal == null) {
            if (other.animal != null)
                return false;
        } else if (!animal.equals(other.animal))
            return false;
        if (vendedor == null) {
            if (other.vendedor != null)
                return false;
        } else if (!vendedor.equals(other.vendedor))
            return false;
        return true;
    }



}
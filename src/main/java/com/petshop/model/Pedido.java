package com.petshop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_pedido")
    private Integer numeroPedido;

    @Column(name = "data_e_hora")
    private LocalDateTime dataEHora;

    // Relacionamento com ItensDePedido (um pedido tem muitos itens)
    // cascade = CascadeType.ALL e orphanRemoval = true garantem que os itens
    // são salvos/excluídos junto com o pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemDePedido> itens = new ArrayList<>();

    // Construtor
    public Pedido() {
    }

    public Pedido(Integer numeroPedido, LocalDateTime dataEHora, List<ItemDePedido> itens) {
        this.numeroPedido = numeroPedido;
        this.dataEHora = dataEHora;
        this.itens = itens;
    }

    // Getters, Setters
    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public LocalDateTime getDataEHora() {
        return dataEHora;
    }

    public void setDataEHora(LocalDateTime dataEHora) {
        this.dataEHora = dataEHora;
    }

    public List<ItemDePedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemDePedido> itens) {
        this.itens = itens;
    }


}
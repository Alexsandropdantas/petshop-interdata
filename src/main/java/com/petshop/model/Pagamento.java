package com.petshop.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "valor_pago")
    private Double valorPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_pedidos_numero_pedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_formas_de_pagamento_id")
    private FormaDePagamento formaDePagamento;

    // --- Construtores
    public Pagamento() {
    }

    public Pagamento(Integer id, Double valorPago, Pedido pedido, FormaDePagamento formaDePagamento) {
        this.id = id;
        this.valorPago = valorPago;
        this.pedido = pedido;
        this.formaDePagamento = formaDePagamento;
    }

    // Getters, Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public FormaDePagamento getFormaDePagamento() {
        return formaDePagamento;
    }

    public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pagamento pagamento = (Pagamento) o;
        return Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", valorPago=" + valorPago +
                '}';
    }
}
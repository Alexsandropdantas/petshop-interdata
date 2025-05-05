package com.petshop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "valor_pago")
    private Double valorPago;

    @Column(name = "data_e_hora")
    private LocalDateTime dataEHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_pedidos_numero_pedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_formas_de_pagamento_id")
    private FormaDePagamento formaDePagamento;

    // --- Construtores
    public Pagamento() {
    }

    public Pagamento(Integer id, Double valorPago, Pedido pedido, FormaDePagamento formaDePagamento, LocalDateTime dataEHora) {
        this.id = id;
        this.valorPago = valorPago;
        this.pedido = pedido;
        this.formaDePagamento = formaDePagamento;
        this.dataEHora = dataEHora;
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

    public LocalDateTime getDataEHora() {
        return dataEHora;
    }

    public void setDataEHora(LocalDateTime dataEHora) {
        this.dataEHora = dataEHora;
    }

    @Override
    public String toString() {
        return "Pagamento [id=" + id + ", valorPago=" + valorPago + ", dataEHora=" + dataEHora + ", pedido=" + pedido
                + ", formaDePagamento=" + formaDePagamento + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((valorPago == null) ? 0 : valorPago.hashCode());
        result = prime * result + ((dataEHora == null) ? 0 : dataEHora.hashCode());
        result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
        result = prime * result + ((formaDePagamento == null) ? 0 : formaDePagamento.hashCode());
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
        Pagamento other = (Pagamento) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (valorPago == null) {
            if (other.valorPago != null)
                return false;
        } else if (!valorPago.equals(other.valorPago))
            return false;
        if (dataEHora == null) {
            if (other.dataEHora != null)
                return false;
        } else if (!dataEHora.equals(other.dataEHora))
            return false;
        if (pedido == null) {
            if (other.pedido != null)
                return false;
        } else if (!pedido.equals(other.pedido))
            return false;
        if (formaDePagamento == null) {
            if (other.formaDePagamento != null)
                return false;
        } else if (!formaDePagamento.equals(other.formaDePagamento))
            return false;
        return true;
    }

}
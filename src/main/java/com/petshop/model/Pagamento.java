package com.petshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "valor_pago", nullable = false)
    private Double valorPago;
    
    @ManyToOne
    @JoinColumn(name = "fk_pedidos_numero_pedido", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "fk_formas_de_pagamento_id", nullable = false)
    private FormaDePagamento formaPagamento;
    
    // Construtores
    public Pagamento() {
    }
    
    public Pagamento(Integer id, Double valorPago, Pedido pedido, FormaDePagamento formaPagamento) {
        this.id = id;
        this.valorPago = valorPago;
        this.pedido = pedido;
        this.formaPagamento = formaPagamento;
    }
    
    // Getters e Setters
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
    
    public FormaDePagamento getFormaPagamento() {
        return formaPagamento;
    }
    
    public void setFormaPagamento(FormaDePagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Pagamento pagamento = (Pagamento) o;
        
        return id != null ? id.equals(pagamento.id) : pagamento.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    // ToString
    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", valorPago=" + valorPago +
                ", pedido=" + (pedido != null ? pedido.getNumeroPedido() : null) +
                ", formaPagamento=" + (formaPagamento != null ? formaPagamento.getId() : null) +
                '}';
    }
}
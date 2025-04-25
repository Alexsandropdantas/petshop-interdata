package com.petshop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_pedido")
    private Integer numeroPedido;
    
    @Column(name = "data_e_hora", nullable = false)
    private LocalDateTime dataEHora;
    
    @Column(nullable = true)
    private Double desconto;
    
    @Transient
    private Double valorTotal; // Calculado com base nos itens e desconto
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pagamento> pagamentos;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itens = new ArrayList<>();
    
    // Construtores
    public Pedido() {
        this.dataEHora = LocalDateTime.now();
        this.desconto = 0.0;
    }
    
    public Pedido(Integer numeroPedido, LocalDateTime dataEHora, Double desconto) {
        this.numeroPedido = numeroPedido;
        this.dataEHora = dataEHora;
        this.desconto = desconto;
    }
    
    // Getters e Setters
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
    
    public Double getDesconto() {
        return desconto;
    }
    
    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }
    
    public Double getValorTotal() {
        // Calcula o valor total com base nos itens
        double total = 0.0;
        if (itens != null) {
            for (ItemPedido item : itens) {
                total += item.getSubtotal();
            }
        }
        
        // Aplica desconto se existir
        if (desconto != null && desconto > 0) {
            total -= desconto;
        }
        
        return total;
    }
    
    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }
    
    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
    
    public List<ItemPedido> getItens() {
        return itens;
    }
    
    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
    
    // Métodos auxiliares
    public Double calcularSaldoDevedor() {
        double totalPago = 0.0;
        if (pagamentos != null) {
            for (Pagamento pagamento : pagamentos) {
                totalPago += pagamento.getValorPago();
            }
        }
        
        return getValorTotal() - totalPago;
    }
    
    public boolean isPagamentoCompleto() {
        return calcularSaldoDevedor() <= 0;
    }
    
    public void adicionarItem(ItemPedido item) {
        if (itens == null) {
            itens = new ArrayList<>();
        }
        
        item.setPedido(this);
        itens.add(item);
    }
    
    public void removerItem(ItemPedido item) {
        if (itens != null) {
            itens.remove(item);
        }
    }
    
    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Pedido pedido = (Pedido) o;
        
        return numeroPedido != null ? numeroPedido.equals(pedido.numeroPedido) : pedido.numeroPedido == null;
    }
    
    @Override
    public int hashCode() {
        return numeroPedido != null ? numeroPedido.hashCode() : 0;
    }
    
    // ToString
    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido=" + numeroPedido +
                ", dataEHora=" + dataEHora +
                ", desconto=" + desconto +
                '}';
    }
}
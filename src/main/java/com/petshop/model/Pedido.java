package com.petshop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assumindo que numero_pedido é auto-incrementável
    @Column(name = "numero_pedido")
    private Integer numeroPedido;

    @Column(name = "data_e_hora")
    private LocalDateTime dataHora;

    @Column
    private Double desconto;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pagamento> pagamentos = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> itens = new ArrayList<>();

    // --- Construtores
    public Pedido() {
    }

    public Pedido(Integer numeroPedido, LocalDateTime dataHora, Double desconto, List<Pagamento> pagamentos,
            List<ItemPedido> itens) {
        this.numeroPedido = numeroPedido;
        this.dataHora = dataHora;
        this.desconto = desconto;
        this.pagamentos = pagamentos;
        this.itens = itens;
    }

    // Getters, Setters
    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
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

    // Método utilitário para adicionar item (bidirecional)
    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
        item.setPedido(this);
    }

    // Método utilitário para remover item (bidirecional)
    public void removerItem(ItemPedido item) {
        this.itens.remove(item);
        item.setPedido(null);
    }

    // Método utilitário para calcular total (exemplo)
    public Double calcularTotalBruto() {
        return itens.stream()
                .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade()).sum();
    }

    public Double calcularTotalComDesconto() {
        double totalBruto = calcularTotalBruto();
        double valorDesconto = totalBruto * (this.desconto != null ? this.desconto / 100.0 : 0.0);
        return totalBruto - valorDesconto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(numeroPedido, pedido.numeroPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroPedido);
    }

    @Override
    public String toString() {
        return "Pedido{" + "numeroPedido=" + numeroPedido + ", DataHora=" + dataHora + ", desconto=" + desconto + '}';
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
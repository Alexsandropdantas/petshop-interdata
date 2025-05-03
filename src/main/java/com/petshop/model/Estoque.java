package com.petshop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

    @Column(name = "nota_de_entrada", length = 30)
    private String notaDeEntrada;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "valor_de_entrada")
    private Double valorDeEntrada;

    @ManyToOne
    @JoinColumn(name = "fk_produtos_id")
    private Produto produto;

    // Construtores
    public Estoque() {
    }

    public Estoque(Integer id, LocalDateTime dataEntrada, String notaDeEntrada, Integer quantidade,
            Double valorDeEntrada, Produto produto) {
        this.id = id;
        this.dataEntrada = dataEntrada;
        this.notaDeEntrada = notaDeEntrada;
        this.quantidade = quantidade;
        this.valorDeEntrada = valorDeEntrada;
        this.produto = produto;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getNotaDeEntrada() {
        return notaDeEntrada;
    }

    public void setNotaDeEntrada(String notaDeEntrada) {
        this.notaDeEntrada = notaDeEntrada;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorDeEntrada() {
        return valorDeEntrada;
    }

    public void setValorDeEntrada(Double valorDeEntrada) {
        this.valorDeEntrada = valorDeEntrada;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Estoque estoque = (Estoque) o;
        return Objects.equals(id, estoque.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Estoque{" +
                "id=" + id +
                ", dataEntrada=" + dataEntrada +
                ", notaDeEntrada='" + notaDeEntrada + '\'' +
                ", quantidade=" + quantidade +
                ", valorDeEntrada=" + valorDeEntrada +
                '}';
    }
}
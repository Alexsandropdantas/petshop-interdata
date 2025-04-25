package com.petshop.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "formas_de_pagamento")
public class FormaDePagamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "descricao", length = 150)
    private String descricao;
    
    @OneToMany(mappedBy = "formaDePagamento")
    private List<Pagamento> pagamentos;
    
    // Construtor padrão
    public FormaDePagamento() {
    }
    
    // Construtor com campos
    public FormaDePagamento(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }
    
    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormaDePagamento that = (FormaDePagamento) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "FormaDePagamento{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
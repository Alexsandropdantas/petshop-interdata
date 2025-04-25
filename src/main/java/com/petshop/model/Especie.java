package com.petshop.model;


import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "especies")
public class Especie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nome", length = 150)
    private String nome;
    
    @OneToMany(mappedBy = "especie")
    private List<Raca> racas;
    
    // Construtor padrão
    public Especie() {
    }
    
    // Construtor com campos
    public Especie(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<Raca> getRacas() {
        return racas;
    }
    
    public void setRacas(List<Raca> racas) {
        this.racas = racas;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Especie especie = (Especie) o;
        return Objects.equals(id, especie.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Especie{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
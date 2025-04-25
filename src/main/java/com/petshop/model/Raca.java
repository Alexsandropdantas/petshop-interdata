package com.petshop.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="racas")
public class Raca {

// Declaração das variáveis
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    
       @ManyToOne
    @JoinColumn(name = "especies_id")
    private Especie especie;
    
    @OneToMany(mappedBy = "raca")
    private List<Animal> animais;

// Construtores
    public Raca(){}


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }
   

    public Raca (Long id, String nome) {
        this.id = id;
        this.nome = nome;

    }

    






 
}
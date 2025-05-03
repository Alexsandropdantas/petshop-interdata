package com.petshop.model;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_de_nascimento")
    private LocalDateTime dataDeNascimento;

    @Column(length = 150)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_clientes_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_racas_id")
    private Raca raca;

    @Column(name = "foto_path", length = 255)
    private String fotoPath;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> itensPedido = new ArrayList<>();

    // --- Construtores
    public Animal() {
    }

    public Animal(Integer id, LocalDateTime dataDeNascimento, String nome, Cliente cliente, Raca raca,
            String fotoPath) {
        this.id = id;
        this.dataDeNascimento = dataDeNascimento;
        this.nome = nome;
        this.cliente = cliente;
        this.raca = raca;
        this.fotoPath = fotoPath;
    }

    // Getters, Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDateTime dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Raca getRaca() {
        return raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public String getIdade() {

        String textoRetorno;

        LocalDateTime dataAtual = LocalDateTime.now();
        if (dataDeNascimento != null) {
            Period periodo = Period.between(this.dataDeNascimento.toLocalDate(), dataAtual.toLocalDate());
            long meses = ChronoUnit.MONTHS.between(this.dataDeNascimento, dataAtual) % 12;

            StringBuilder sb = new StringBuilder();
            if (periodo.getYears() > 0) {
                sb.append(periodo.getYears());
                sb.append(periodo.getYears() == 1 ? " ano" : " anos");
            }
            if (meses > 0) {
                if (sb.length() > 0) {
                    sb.append(" e ");
                }
                sb.append(meses);
                sb.append(meses == 1 ? " mês" : " meses");
            }
            textoRetorno = sb.toString().isEmpty() ? "Menos de 1 mês" : sb.toString();
        } else {
            textoRetorno = "Sem data de Nascimento";
        }
        return textoRetorno;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((dataDeNascimento == null) ? 0 : dataDeNascimento.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        result = prime * result + ((raca == null) ? 0 : raca.hashCode());
        result = prime * result + ((fotoPath == null) ? 0 : fotoPath.hashCode());
        result = prime * result + ((itensPedido == null) ? 0 : itensPedido.hashCode());
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
        Animal other = (Animal) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (dataDeNascimento == null) {
            if (other.dataDeNascimento != null)
                return false;
        } else if (!dataDeNascimento.equals(other.dataDeNascimento))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        if (raca == null) {
            if (other.raca != null)
                return false;
        } else if (!raca.equals(other.raca))
            return false;
        if (fotoPath == null) {
            if (other.fotoPath != null)
                return false;
        } else if (!fotoPath.equals(other.fotoPath))
            return false;
        if (itensPedido == null) {
            if (other.itensPedido != null)
                return false;
        } else if (!itensPedido.equals(other.itensPedido))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Animal [id=" + id + ", dataDeNascimento=" + dataDeNascimento + ", nome=" + nome + ", cliente=" + cliente
                + ", raca=" + raca + ", fotoPath=" + fotoPath + ", itensPedido=" + itensPedido + "]";
    }

}
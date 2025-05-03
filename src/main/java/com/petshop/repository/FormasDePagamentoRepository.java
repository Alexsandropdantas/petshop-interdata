package com.petshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.model.FormaDePagamento;

@Repository
public interface FormasDePagamentoRepository extends JpaRepository<FormaDePagamento, Integer> {
    
    Optional<FormaDePagamento> findByDescricao(String descricao);
    
}
package com.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.model.Estoque;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {}


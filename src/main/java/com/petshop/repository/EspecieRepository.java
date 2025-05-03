package com.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.model.Especie;

@Repository
public interface EspecieRepository extends JpaRepository<Especie, Integer> {}

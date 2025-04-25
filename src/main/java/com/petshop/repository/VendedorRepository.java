package com.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.model.Vendedor;




@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {}



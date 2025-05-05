package com.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.petshop.model.Estoque;
import com.petshop.model.Produto;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {


    List<Estoque> findByProduto(Produto produto);
    
    @Query("SELECT SUM(e.quantidade) FROM Estoque e WHERE e.produto.id = :produtoId")
    Integer getTotalQuantidadeByProdutoId(@Param("produtoId") Integer produtoId);
    
    @Query("SELECT e FROM Estoque e WHERE e.produto.id = :produtoId ORDER BY e.dataEntrada DESC")
    List<Estoque> findByProdutoIdOrderByDataEntradaDesc(@Param("produtoId") Integer produtoId);

}


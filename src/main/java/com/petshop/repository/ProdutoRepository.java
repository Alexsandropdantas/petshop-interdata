package com.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.petshop.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {


    @Query(value = "SELECT " +
                "p.id AS produtoId, " +
                "p.nome AS nomeProduto, " +
                "COUNT(i.fk_produtos_id) AS quantidadeVendida " +
                "FROM itens_de_pedidos_vendedores_produtos_clientes_animais_pedidos i " +
                "JOIN produtos p ON i.fk_produtos_id = p.id " +
                "GROUP BY p.id, p.nome " +
                "ORDER BY quantidadeVendida DESC", nativeQuery = true)
    List<Object[]> rankingProdutosMaisVendidos();


}
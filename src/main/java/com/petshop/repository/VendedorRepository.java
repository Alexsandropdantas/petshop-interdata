package com.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.petshop.model.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {
        
        @Query(value = "SELECT " +
                   "    v.nome AS nome_vendedor, " +
                   "    SUM(p.preco) AS total_vendido " +
                   "FROM vendedores AS v " +
                   "JOIN itens_de_pedidos AS ip ON v.id = ip.fk_vendedores_id " +
                   "JOIN produtos AS p ON ip.fk_produtos_id = p.id " +
                   "GROUP BY v.id, v.nome " +
                   "ORDER BY total_vendido DESC",
           nativeQuery = true)
    List<Object[]> findVendedoresTotalVendido();
}



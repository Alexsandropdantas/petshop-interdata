package com.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.petshop.model.ItemDePedido;

import jakarta.transaction.Transactional;



@Repository
public interface ItemDePedidoRepository extends JpaRepository<ItemDePedido, Integer> {
    
    
    List<ItemDePedido> findByPedidoNumeroPedido(Integer numeroPedido); 

    @Transactional // Necessário para operações de delete derivadas pelo nome do método
    void deleteByPedidoNumeroPedido(Integer numeroPedido);

   /**
     * Retorna apenas o número do pedido de um item de pedido específico
     * @param itemId o ID do item de pedido
     * @return O número do pedido como Integer
     */
    @Query("SELECT i.pedido.numeroPedido FROM ItemDePedido i WHERE i.id = :itemId")
    Integer findNumeroPedidoByItemId(@Param("itemId") Integer itemId);

}
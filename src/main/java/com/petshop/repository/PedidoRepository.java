package com.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.petshop.model.Pedido;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByDataEHoraBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    // @Query("SELECT p FROM Pedido p WHERE p.pagamento IS NULL")
    // List<Pedido> findPedidosSemPagamento();

    // @Query("SELECT p FROM Pedido p WHERE p.pagamento.valorPago < p.valorTotal")
    // List<Pedido> findPedidosComPagamentoParcial();
}
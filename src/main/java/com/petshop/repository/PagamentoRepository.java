package com.petshop.repository;

import com.petshop.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
    List<Pagamento> findByPedidoNumeroPedido(Integer numeroPedido);
    List<Pagamento> findByValorPago(Double valor);
    List<Pagamento> findByFormaDePagamento_Id(Integer id);
    
    @Query("SELECT p FROM Pagamento p WHERE p.formaDePagamento.id = :id")
    List<Pagamento> findByFormaDePagamentoId(@Param("id") Integer id);

    @Query("SELECT SUM(p.valorPago) FROM Pagamento p WHERE p.pedido.numeroPedido = :numeroPedido")
    Double sumValorPagoByPedidoNumeroPedido(@Param("numeroPedido") Integer numeroPedido);
}
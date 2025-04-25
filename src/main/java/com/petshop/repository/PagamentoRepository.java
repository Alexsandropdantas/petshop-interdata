package com.petshop.repository;

import com.petshop.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
    List<Pagamento> findByPedidoNumeroPedido(Integer numeroPedido);
    List<Pagamento> findByFormaDePagamentoId(Integer formaPagamentoId);
    List<Pagamento> findByValorPago(Double valor);
}
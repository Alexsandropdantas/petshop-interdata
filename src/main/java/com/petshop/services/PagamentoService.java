package com.petshop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.Pagamento;
import com.petshop.repository.PagamentoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public Pagamento buscarPorId(Integer id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com ID: " + id));
    }

    public List<Pagamento> buscarPorPedido(Integer numeroPedido) {
        return pagamentoRepository.findByPedidoNumeroPedido(numeroPedido);
    }

    public List<Pagamento> buscarPorFormaPagamento(Integer formaPagamentoId) {
        return pagamentoRepository.findByFormaDePagamentoId(formaPagamentoId);
    }

    public Pagamento salvar(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento atualizar(Integer id, Pagamento pagamentoAtualizado) {
        if (!pagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pagamento não encontrado com ID: " + id);
        }
        
        pagamentoAtualizado.setId(id);
        return pagamentoRepository.save(pagamentoAtualizado);
    }

    public void excluir(Integer id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pagamento não encontrado com ID: " + id);
        }
        
        pagamentoRepository.deleteById(id);
    }

    public Double calcularTotalPagamentoPorPedido(Integer numeroPedido) {
        List<Pagamento> pagamentos = pagamentoRepository.findByPedidoNumeroPedido(numeroPedido);
        double total = 0.0;
        for (Pagamento pagamento : pagamentos) {
            total += pagamento.getValorPago();
        }
        return total;
    }
}
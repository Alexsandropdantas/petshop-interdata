package com.petshop.services;

import com.petshop.model.FormaDePagamento;
import com.petshop.model.Pagamento;
import com.petshop.model.Pedido;
import com.petshop.repository.PagamentoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoService pedidoService;
    private final FormasDePagamentoService formaDePagamentoService;

    public PagamentoService(PagamentoRepository pagamentoRepository, PedidoService pedidoService,
            FormasDePagamentoService formaDePagamentoService) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoService = pedidoService;
        this.formaDePagamentoService = formaDePagamentoService;
    }

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public Pagamento buscarPorId(Integer id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com ID: " + id));
    }

    public List<Pagamento> buscarPorPedido(Integer numeroPedido) {
        pedidoService.buscarPorId(numeroPedido);
        return pagamentoRepository.findByPedidoNumeroPedido(numeroPedido);
    }

    public Double calcularTotalPagamentoPorPedido(Integer numeroPedido) {
        Double total = pagamentoRepository.sumValorPagoByPedidoNumeroPedido(numeroPedido);
        return total != null ? total : 0.0;
    }

    public List<Pagamento> findByFormaDePagamentoId(Integer formaDePagamentoId) {
        formaDePagamentoService.buscarPorId(formaDePagamentoId);
        return pagamentoRepository.findByFormaDePagamentoId(formaDePagamentoId);
    }

    @Transactional
    public Pagamento salvar(Pagamento pagamento) {
        validarEAnexarEntidades(pagamento);
        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Pagamento atualizar(Integer id, Pagamento pagamentoAtualizado) {
        Pagamento pagamentoExistente = buscarPorId(id);
        validarEAnexarEntidades(pagamentoAtualizado);
        pagamentoExistente.setValorPago(pagamentoAtualizado.getValorPago());
        pagamentoExistente.setPedido(pagamentoAtualizado.getPedido());
        pagamentoExistente.setFormaDePagamento(pagamentoAtualizado.getFormaDePagamento());

        return pagamentoRepository.save(pagamentoExistente);
    }

    @Transactional
    public void excluir(Integer id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pagamento não encontrado com ID: " + id);
        }
        pagamentoRepository.deleteById(id);
    }

    private void validarEAnexarEntidades(Pagamento pagamento) {
        if (pagamento.getPedido() == null || pagamento.getPedido().getNumeroPedido() == null) {
            throw new IllegalArgumentException("Pedido é obrigatório para o pagamento.");
        }
        Pedido pedido = pedidoService.buscarPorId(pagamento.getPedido().getNumeroPedido());
        pagamento.setPedido(pedido);

        if (pagamento.getFormaDePagamento() == null || pagamento.getFormaDePagamento().getId() == null) {
            throw new IllegalArgumentException("Forma de Pagamento é obrigatória para o pagamento.");
        }
        FormaDePagamento forma = formaDePagamentoService.buscarPorId(pagamento.getFormaDePagamento().getId());
        pagamento.setFormaDePagamento(forma);

        if (pagamento.getValorPago() == null || pagamento.getValorPago() <= 0) {
            throw new IllegalArgumentException("Valor do pagamento deve ser positivo.");
        }
    }

    public List<Map<String, Object>> getTotalVendasPorMes2024() {
        List<Object[]> resultados = pagamentoRepository.findTotalVendasPorMes2024();
        List<Map<String, Object>> vendasPorMesList = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Map<String, Object> vendaMesMap = new HashMap<>();
            vendaMesMap.put("mes", (String) resultado[0]); 
    
            vendaMesMap.put("totalVendas", (Double) resultado[1]);

            vendasPorMesList.add(vendaMesMap);
        }

        return vendasPorMesList;
    }
}
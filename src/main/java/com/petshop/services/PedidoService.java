package com.petshop.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.ItemPedido;
import com.petshop.model.Pedido;
import com.petshop.repository.ItemPedidoRepository;
import com.petshop.repository.PedidoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Integer numeroPedido) {
        return pedidoRepository.findById(numeroPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com número: " + numeroPedido));
    }

    public List<Pedido> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return pedidoRepository.findByDataEHoraBetween(dataInicio, dataFim);
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        // Se for um novo pedido, define a data atual
        if (pedido.getDataEHora() == null) {
            pedido.setDataEHora(LocalDateTime.now());
        }
        
        // Se não tiver desconto definido, inicializa com zero
        if (pedido.getDesconto() == null) {
            pedido.setDesconto(0.0);
        }
        
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido atualizar(Integer numeroPedido, Pedido pedidoAtualizado) {
        if (!pedidoRepository.existsById(numeroPedido)) {
            throw new EntityNotFoundException("Pedido não encontrado com número: " + numeroPedido);
        }
        
        pedidoAtualizado.setNumeroPedido(numeroPedido);
        return pedidoRepository.save(pedidoAtualizado);
    }

    @Transactional
    public void excluir(Integer numeroPedido) {
        if (!pedidoRepository.existsById(numeroPedido)) {
            throw new EntityNotFoundException("Pedido não encontrado com número: " + numeroPedido);
        }
        
        pedidoRepository.deleteById(numeroPedido);
    }

    @Transactional
    public void removerItem(Integer numeroPedido, Long itemId) {
        // Verifica se o pedido existe
        Pedido pedido = buscarPorId(numeroPedido);
        
        // Busca o item pelo ID
        Optional<ItemPedido> itemOpt = itemPedidoRepository.findById(itemId);
        if (!itemOpt.isPresent()) {
            throw new EntityNotFoundException("Item não encontrado com ID: " + itemId);
        }
        
        ItemPedido item = itemOpt.get();
        
        // Verifica se o item pertence ao pedido informado
        if (!item.getPedido().getNumeroPedido().equals(numeroPedido)) {
            throw new IllegalArgumentException("O item não pertence ao pedido informado");
        }
        
        // Remove o item do pedido e do banco de dados
        pedido.getItens().remove(item);
        itemPedidoRepository.delete(item);
    }

    public double calcularValorTotalPedido(Integer numeroPedido) {
        Pedido pedido = buscarPorId(numeroPedido);
        return pedido.getValorTotal();
    }


    public List<Pedido> buscarPedidosSemPagamento() {
        return pedidoRepository.findPedidosSemPagamento();
    }
    
    public List<Pedido> buscarPedidosComPagamentoParcial() {
        return pedidoRepository.findPedidosComPagamentoParcial();
    }

}
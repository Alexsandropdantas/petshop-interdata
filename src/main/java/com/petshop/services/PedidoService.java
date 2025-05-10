package com.petshop.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.Pedido;
import com.petshop.repository.PedidoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> buscarTodosOsPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Integer numeroPedido) {
        return pedidoRepository.findById(numeroPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + numeroPedido));
    }

    @Transactional // Garante que a operação seja atômica
    public Pedido criarNovoPedido() {
        Pedido novoPedido = new Pedido();
        novoPedido.setDataEHora(LocalDateTime.now());
        // Salva o novo pedido no banco de dados. O JPA/Hibernate
        // preencherá o numeroPedido (ID) automaticamente.
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);

        return pedidoSalvo; // Retorna o objeto Pedido completo, já com o ID
    }

    @Transactional
    public void excluir(Integer numeroPedido) {
        if (!pedidoRepository.existsById(numeroPedido)) {
            throw new EntityNotFoundException("Pedido não encontrado com ID: " + numeroPedido);
        }
        // A exclusão em cascata deve remover itens e pagamentos associados
        pedidoRepository.deleteById(numeroPedido);
    }



}
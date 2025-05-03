package com.petshop.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petshop.model.Animal;
import com.petshop.model.Cliente;
import com.petshop.model.ItemPedido;
import com.petshop.model.Pedido;
import com.petshop.model.Produto;
import com.petshop.model.Vendedor;
import com.petshop.repository.ItemPedidoRepository;
import com.petshop.repository.PedidoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    // Injetar outros services necessários (Produto, Vendedor, Cliente, Animal)
    private final ProdutoService produtoService;
    private final VendedorService vendedorService;
    private final ClienteService clienteService;
    private final AnimalService animalService;

    public PedidoService(PedidoRepository pedidoRepository, ItemPedidoRepository itemPedidoRepository,
            ProdutoService produtoService, VendedorService vendedorService, ClienteService clienteService,
            AnimalService animalService) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoService = produtoService;
        this.vendedorService = vendedorService;
        this.clienteService = clienteService;
        this.animalService = animalService;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Integer numeroPedido) {
        return pedidoRepository.findById(numeroPedido);
    }

    public Pedido buscarPorId(Integer numeroPedido) {
        return findById(numeroPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + numeroPedido));
    }

    @Transactional // Garante atomicidade
    public Pedido salvar(Pedido pedido) {
        if (pedido.getNumeroPedido() == null) {
            pedido.setDataHora(LocalDateTime.now());
            if (pedido.getDesconto() == null)
                pedido.setDesconto(0.0);
        }
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido atualizar(Integer numeroPedido, Pedido pedidoAtualizado) {
        Pedido pedidoExistente = buscarPorId(numeroPedido);
        pedidoExistente.setDesconto(pedidoAtualizado.getDesconto());
        // A data e hora não devem ser atualizadas aqui geralmente
        // Os itens são gerenciados separadamente

        // Limpa itens existentes e adiciona os novos (cuidado com performance em muitos
        // itens)
        // Ou implementa lógica mais granular para adicionar/remover/atualizar itens
        pedidoExistente.getItens().clear();
        itemPedidoRepository.deleteAll(pedidoExistente.getItens()); // Remove órfãos

        if (pedidoAtualizado.getItens() != null) {
            for (ItemPedido itemNovo : pedidoAtualizado.getItens()) {
                itemNovo.setPedido(pedidoExistente); // Associa ao pedido existente
                // Valida e busca entidades relacionadas ANTES de salvar o item
                validarEAnexarEntidadesItem(itemNovo);
                itemPedidoRepository.save(itemNovo); // Salva o novo item
                pedidoExistente.getItens().add(itemNovo); // Adiciona à coleção do pedido
            }
        }

        return pedidoRepository.save(pedidoExistente);
    }

    @Transactional
    public void excluir(Integer numeroPedido) {
        if (!pedidoRepository.existsById(numeroPedido)) {
            throw new EntityNotFoundException("Pedido não encontrado com ID: " + numeroPedido);
        }
        // A exclusão em cascata deve remover itens e pagamentos associados
        pedidoRepository.deleteById(numeroPedido);
    }

    @Transactional
    public Pedido adicionarItemAoPedido(Integer numeroPedido, ItemPedido itemPedido) {
        Pedido pedido = buscarPorId(numeroPedido);

        // Valida e busca entidades relacionadas ANTES de salvar o item
        validarEAnexarEntidadesItem(itemPedido);

        // Define preço unitário se não vier do form (baseado no produto)
        if (itemPedido.getPrecoUnitario() == null || itemPedido.getPrecoUnitario() <= 0) {
            if (itemPedido.getProduto() != null && itemPedido.getProduto().getPreco() != null) {
                itemPedido.setPrecoUnitario(itemPedido.getProduto().getPreco());
            } else {
                throw new IllegalArgumentException("Não foi possível determinar o preço unitário do item.");
            }
        }

        // Garante quantidade mínima
        if (itemPedido.getQuantidade() == null || itemPedido.getQuantidade() <= 0) {
            itemPedido.setQuantidade(1);
        }

        pedido.adicionarItem(itemPedido); // Adiciona na coleção e seta relação bidirecional
        itemPedidoRepository.save(itemPedido); // Salva o item individualmente também
        // return pedidoRepository.save(pedido); // Salvar o pedido pode não ser
        // necessário se a cascata estiver correta
        return pedido; // Retorna o pedido atualizado (em memória)
    }

    @Transactional
    public void removerItem(Integer numeroPedido, Integer itemId) {
        Pedido pedido = buscarPorId(numeroPedido);
        ItemPedido itemParaRemover = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item de pedido não encontrado com ID: " + itemId));

        if (!itemParaRemover.getPedido().getNumeroPedido().equals(numeroPedido)) {
            throw new IllegalArgumentException("Item não pertence ao pedido especificado.");
        }

        pedido.removerItem(itemParaRemover); // Remove da coleção e quebra relação bidirecional
        itemPedidoRepository.delete(itemParaRemover); // Exclui o item do banco
        // pedidoRepository.save(pedido); // Pode ser necessário se a cascata não
        // funcionar como esperado
    }

    // Método auxiliar para validar e buscar entidades do ItemPedido
    private void validarEAnexarEntidadesItem(ItemPedido item) {
        if (item.getProduto() == null || item.getProduto().getId() == null) {
            throw new IllegalArgumentException("Produto é obrigatório para o item do pedido.");
        }
        Produto produto = produtoService.buscarPorIdOuFalhar(item.getProduto().getId());
        item.setProduto(produto);

        if (item.getVendedor() == null || item.getVendedor().getId() == null) {
            throw new IllegalArgumentException("Vendedor é obrigatório para o item do pedido.");
        }
        Vendedor vendedor = vendedorService.buscarPorIdOuFalhar(item.getVendedor().getId());
        item.setVendedor(vendedor);

        if (item.getCliente() == null || item.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório para o item do pedido.");
        }
        Cliente cliente = clienteService.buscarPorId(item.getCliente().getId());
        item.setCliente(cliente);

        // Animal é opcional
        if (item.getAnimal() != null && item.getAnimal().getId() != null) {
            Animal animal = animalService.buscarPorIdOuFalhar(item.getAnimal().getId());
            item.setAnimal(animal);
        } else {
            item.setAnimal(null); // Garante que seja nulo se não fornecido ID
        }
    }

}
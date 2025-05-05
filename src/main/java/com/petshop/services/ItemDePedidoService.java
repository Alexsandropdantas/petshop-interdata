package com.petshop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.Animal;
import com.petshop.model.Cliente;
import com.petshop.model.ItemDePedido;
import com.petshop.model.Pedido;
import com.petshop.model.Produto;
import com.petshop.model.Vendedor;
import com.petshop.repository.ItemDePedidoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ItemDePedidoService {

    private final ItemDePedidoRepository itemDePedidoRepository;
    private PedidoService pedidoService;
    private ProdutoService produtoService; 
    private ClienteService clienteService;
    private AnimalService animalService;
    private VendedorService vendedorService;

    @Autowired
    public ItemDePedidoService(ItemDePedidoRepository itemDePedidoRepository, 
                                PedidoService pedidoService, 
                                ProdutoService produtoService, 
                                ClienteService clienteService,
                                AnimalService animalService,
                                VendedorService vendedorService) {

        this.itemDePedidoRepository = itemDePedidoRepository;
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
        this.animalService = animalService;
        this.vendedorService = vendedorService;
                                }

    public List<ItemDePedido> listarTodos() {
        return itemDePedidoRepository.findAll();
    }

    public ItemDePedido buscarPorId(Integer itemDePedido) {
        return itemDePedidoRepository.findById(itemDePedido)
                .orElseThrow(() -> new EntityNotFoundException("Item do Pedido não encontrado com ID: " + itemDePedido));
    }

    public List<ItemDePedido> buscarPorNumeroDoPedido(Integer numeroDoPedido) {
        return itemDePedidoRepository.findByPedidoNumeroPedido(numeroDoPedido);
    }

    public void excluirItemDePedidoPorId(Integer id) {
        itemDePedidoRepository.deleteById(id);
    }

    public void adicionarItemDePedido(Integer numeroPedido, Integer produtoId, Integer quantidade,
            Integer clienteId, Integer animalId, Integer vendedorId, Double desconto) {

        ItemDePedido item = new ItemDePedido();

        Pedido pedido = pedidoService.buscarPorId(numeroPedido);
        item.setPedido(pedido);

        Produto produto = produtoService.buscarPorId(produtoId);
        item.setProduto(produto);

        item.setQuantidade(quantidade);

        Cliente cliente = clienteService.buscarPorId(clienteId);
        item.setCliente(cliente);

        Animal animal = animalService.buscarPorId(animalId);
        item.setAnimal(animal);

        Vendedor vendedor = vendedorService.buscarPorId(vendedorId);
        item.setVendedor(vendedor);

        item.setDesconto(desconto);

        itemDePedidoRepository.save(item);

     

    }



}
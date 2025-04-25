package com.petshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.Cliente;
import com.petshop.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Listar todos os clientes
    public List<Cliente> buscarTodosOsClientes() {
        return clienteRepository.findAll();
    }

    public void salvarCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    // Buscar um cliente por ID
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Deletar um cliente
    public void excluirClientePorId(Long id) {
        clienteRepository.deleteById(id);
    }

    // Editar cliente (atualizar suas informações)
    public Cliente atualizarCliente(Cliente cliente) {
        if (cliente.getId() != null) {
            return clienteRepository.save(cliente);  // aqui chamamos o método save acima
        }
        return null;
    }


}
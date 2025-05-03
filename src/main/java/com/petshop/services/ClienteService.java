package com.petshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.petshop.model.Cliente;
import com.petshop.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    

    public List<Cliente> buscarTodosOsClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id) { // ID é Integer
        return clienteRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
    }


    public Cliente salvarCliente(Cliente cliente) {
        // Se for uma atualização, buscar o cliente existente e atualiza os campos exceto ID
         if (cliente.getId() != null) {
             Cliente existente = buscarPorId(cliente.getId());
             existente.setNome(cliente.getNome());
             existente.setEmail(cliente.getEmail());
             existente.setTelefone(cliente.getTelefone());
             existente.setEndereco(cliente.getEndereco());
             if (cliente.getFotoPath() != null && !cliente.getFotoPath().isBlank()) {
                 existente.setFotoPath(cliente.getFotoPath());
             }
             return clienteRepository.save(existente);
         } else {
            // Novo cliente
            return clienteRepository.save(cliente);
         }
    }

    public void excluirClientePorId(Integer id) {
        clienteRepository.deleteById(id);
    }
}
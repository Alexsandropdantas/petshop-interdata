package com.petshop.services;

import com.petshop.model.Vendedor;
import com.petshop.repository.VendedorRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendedorService {

    private final VendedorRepository vendedorRepository;

    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    public List<Vendedor> buscarTodosOsVendedores() {
        return vendedorRepository.findAll();
    }

    public Vendedor buscarPorId(Integer id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor não encontrado com ID: " + id));
    }


    public Vendedor salvarVendedor(Vendedor vendedor) {
        if (vendedor.getId() != null) {
            Vendedor existente = buscarPorId(vendedor.getId());
            existente.setNome(vendedor.getNome());
            existente.setEmail(vendedor.getEmail());
            existente.setTelefone(vendedor.getTelefone());
            return vendedorRepository.save(existente);
        } else {
            return vendedorRepository.save(vendedor);
        }
    }

    public void excluirVendedorPorId(Integer id) {
        if (!vendedorRepository.existsById(id)) {
            throw new EntityNotFoundException("Vendedor não encontrado com ID: " + id);
        }
        // Adicionar verificação de dependência (Itens de Pedido) se necessário
        vendedorRepository.deleteById(id);
    }
}
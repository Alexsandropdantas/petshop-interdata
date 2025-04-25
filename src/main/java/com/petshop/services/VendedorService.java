package com.petshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.Vendedor;  
import com.petshop.repository.VendedorRepository;  

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository vendedorRepository;  

    public List<Vendedor> buscarTodosOsVendedores() {
        return vendedorRepository.findAll();
    }

    public void salvarVendedor(Vendedor vendedor) {
        vendedorRepository.save(vendedor);
    }

    public Optional<Vendedor> buscarPorId(Long id) {
        return vendedorRepository.findById(id);
    }

    public void excluirVendedorPorId(Long id) {
        vendedorRepository.deleteById(id);
    }

    public Vendedor atualizarVendedor(Vendedor vendedor) {  
        if (vendedor.getId() != null) {
            return vendedorRepository.save(vendedor);  
        }
        return null;  
    }
}

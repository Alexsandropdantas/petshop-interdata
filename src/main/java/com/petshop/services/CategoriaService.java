package com.petshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.Categoria;
import com.petshop.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    
    public List<Categoria> buscarTodosAsCategorias() {
        return categoriaRepository.findAll();
    }

   
    public void salvarCategoria(Categoria categoria) {
        categoriaRepository.save(categoria); 
    }

 
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

  
    public void excluirCategoriaPorId(Long id) {
        categoriaRepository.deleteById(id);
    }

   
    public Categoria atualizarCategoria(Categoria categoria) {
        if (categoria.getId() != null) {
            return categoriaRepository.save(categoria);  
        }
        return null;
    }


}
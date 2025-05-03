package com.petshop.services;

import com.petshop.model.Categoria;
import com.petshop.repository.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> buscarTodosAsCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria buscarPorIdOuFalhar(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));
    }

    public Categoria salvarCategoria(Categoria categoria) {
        // Lógica para atualização e criação de categoria
        if (categoria.getId() != null) {
            Categoria existente = buscarPorIdOuFalhar(categoria.getId());
            existente.setNome(categoria.getNome());
            return categoriaRepository.save(existente);
        } else {
            return categoriaRepository.save(categoria);
        }
    }

    public void excluirCategoriaPorId(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoria não encontrada com ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
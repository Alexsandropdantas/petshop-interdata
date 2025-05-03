package com.petshop.services;

import com.petshop.model.Categoria;
import com.petshop.model.Produto;
import com.petshop.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService; // Para buscar categoria

    // Usar @Lazy para quebrar dependência circular se CategoriaService também depender de ProdutoService
    public ProdutoService(ProdutoRepository produtoRepository, @Lazy CategoriaService categoriaService) {
        this.produtoRepository = produtoRepository;
        this.categoriaService = categoriaService;
    }

    public List<Produto> buscarTodosOsProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Integer id) { // Usar Integer
        return produtoRepository.findById(id);
    }

    public Produto buscarPorIdOuFalhar(Integer id) { // Usar Integer
        return buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
    }

    public Produto salvarProduto(Produto produto) {
        // Validar e anexar Categoria
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória para o produto.");
        }
        Categoria categoria = categoriaService.buscarPorIdOuFalhar(produto.getCategoria().getId());
        produto.setCategoria(categoria);

        if (produto.getPreco() == null || produto.getPreco() < 0) {
             throw new IllegalArgumentException("Preço do produto não pode ser negativo.");
        }

        // Lógica de atualização vs criação
        if (produto.getId() != null) {
            Produto existente = buscarPorIdOuFalhar(produto.getId());
            existente.setNome(produto.getNome());
            existente.setPreco(produto.getPreco());
            existente.setCategoria(produto.getCategoria()); // Atualiza categoria
            if (produto.getFotoPath() != null && !produto.getFotoPath().isBlank()) {
                existente.setFotoPath(produto.getFotoPath());
            }
            return produtoRepository.save(existente);
        } else {
            return produtoRepository.save(produto);
        }
    }

    public void excluirProdutoPorId(Integer id) { // Usar Integer
        if (!produtoRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto não encontrado com ID: " + id);
        }
        // Adicionar verificação de dependência (Itens de Pedido, Estoque) se necessário
        produtoRepository.deleteById(id);
    }
}
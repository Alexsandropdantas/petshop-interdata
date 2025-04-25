package com.petshop.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.petshop.model.Produto;
import com.petshop.services.CategoriaService;
import com.petshop.services.ProdutoService;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaService categoriaService;
    
    @Value("${imagens.produtos.path}")
    private String imagesPath;

    @GetMapping("/produtos")
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
        return "produtos/lista";
    }

    @GetMapping("/produtos/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("categorias", categoriaService.buscarTodosAsCategorias());
        return "produtos/cadastro";
    }


    @GetMapping("/produtos/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {
        Produto produto = produtoService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID produto inválido: " + id));
        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaService.buscarTodosAsCategorias());
        return "produtos/editar";
    }

    @PostMapping("/produtos/editar/{id}")
    public String atualizarProduto(@PathVariable Long id, @ModelAttribute Produto produtoAtualizado) {
        Produto produto = produtoService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID produto inválido: " + id));
        produto.setNome(produtoAtualizado.getNome());
        produto.setPreco(produtoAtualizado.getPreco());
        produtoService.salvarProduto(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/produtos/deletar/{id}")
    public String deletarproduto(@PathVariable Long id) {
        produtoService.excluirProdutoPorId(id);
        return "redirect:/produtos";
    }

    // Podemos salvar sem a foto por isso verificamos se a foto veio vazia com o método isEmpty()
    @PostMapping("/produtos")
    public String salvarproduto(@ModelAttribute Produto produto, @RequestParam("foto") MultipartFile foto) throws IOException {
        if (!foto.isEmpty()) {
            String nomeArquivo = foto.getOriginalFilename();  // adicionar uma chave tipo data e hora
            Path caminho = Paths.get(imagesPath + nomeArquivo);
            Files.copy(foto.getInputStream(), caminho);
            produto.setFotoPath(caminho.toString());
        }
        produtoService.salvarProduto(produto);
        return "redirect:/produtos";
    }

}

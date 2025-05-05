package com.petshop.controllers;

import com.petshop.comum.BibliotecaDeMetodosComunsAoSistema;
import com.petshop.model.Categoria;
import com.petshop.model.Produto;
import com.petshop.services.CategoriaService;
import com.petshop.services.ProdutoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService; // Injetar ProdutoService
    @Autowired
    private CategoriaService categoriaService; // Injetar CategoriaService

    @Value("${imagens.produtos.path}")
    private String imagesPath;

    @GetMapping
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
        return "produtos/lista";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("categorias", categoriaService.buscarTodosAsCategorias());
        return "produtos/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Produto produto = produtoService.buscarPorId(id);
            model.addAttribute("produto", produto);
            model.addAttribute("categorias", categoriaService.buscarTodosAsCategorias());
            return "produtos/editar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Produto não encontrado com ID: " + id);
            return "redirect:/produtos";
        }
    }

    @PostMapping // Salvar no POST /produtos
    public String salvarProduto(@ModelAttribute Produto produto,
            @RequestParam("categoriaId") Integer categoriaId,
            @RequestParam("foto") MultipartFile foto,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Busca e associa a Categoria
            Categoria categoria = categoriaService.buscarPorIdOuFalhar(categoriaId);
            produto.setCategoria(categoria);

            if (!foto.isEmpty()) {
                String nomeUUID = UUID.randomUUID().toString().replace("-", "");
                Path diretorioPath = Paths.get(imagesPath);
                Files.createDirectories(diretorioPath);
                Path caminhoArquivo = diretorioPath.resolve(nomeUUID);
                Files.copy(foto.getInputStream(), caminhoArquivo);

                // Salva o caminho no banco removendo src\main\resources\static\
                produto.setFotoPath(BibliotecaDeMetodosComunsAoSistema.caminhoDasImagensWeb(caminhoArquivo.toString()));
            }

            // O service fará a lógica para salvar o produto
            produtoService.salvarProduto(produto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto salvo com sucesso!");
            return "redirect:/produtos";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar a foto do produto.");
            // Logar erro
            model.addAttribute("produto", produto);
            model.addAttribute("categorias", categoriaService.buscarTodosAsCategorias());
            return "produtos/cadastro";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar produto: " + e.getMessage());
            // Logar erro
            model.addAttribute("produto", produto);
            model.addAttribute("categorias", categoriaService.buscarTodosAsCategorias());
            return "produtos/cadastro";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizarProduto(@PathVariable Integer id,
            @ModelAttribute Produto produtoAtualizado,
            @RequestParam("categoriaId") Integer categoriaId,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            produtoAtualizado.setId(id);

            // Busca e associa a Categoria
            Categoria categoria = categoriaService.buscarPorIdOuFalhar(categoriaId);
            produtoAtualizado.setCategoria(categoria);

            // Lida com a foto apenas se uma nova foi enviada
            if (foto != null && !foto.isEmpty()) {
                String nomeArquivo = System.currentTimeMillis() + "_"+ foto.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
                Path diretorioPath = Paths.get(imagesPath);
                Files.createDirectories(diretorioPath);
                Path caminhoArquivo = diretorioPath.resolve(nomeArquivo);
                Files.copy(foto.getInputStream(), caminhoArquivo);
                produtoAtualizado.setFotoPath(caminhoArquivo.toString());
            } else {
                // Mantém a foto existente
                Produto produtoExistente = produtoService.buscarPorId(id);
                produtoAtualizado.setFotoPath(produtoExistente.getFotoPath());
            }

            // O service salvarProduto fará a lógica para atualizar o produto
            produtoService.salvarProduto(produtoAtualizado);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto atualizado com sucesso!");
            return "redirect:/produtos";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar a foto do produto.");
            model.addAttribute("produto", produtoAtualizado);
            model.addAttribute("categorias", categoriaService.buscarTodosAsCategorias());
            return "produtos/editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar produto: " + e.getMessage());
            return "redirect:/produtos/editar/" + id;
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarProduto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            produtoService.excluirProdutoPorId(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir produto: " + e.getMessage());
        }
        return "redirect:/produtos";
    }
}

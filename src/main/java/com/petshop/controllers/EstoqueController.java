package com.petshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.model.Produto;
import com.petshop.services.EstoqueService;
import com.petshop.services.ProdutoService;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public String listarProdutosEmEstoque(Model model) {
        model.addAttribute("estoque", estoqueService.buscarEstoqueDeProdutos());
        return "estoque/lista";
    }

    @GetMapping("/entrada")
    public String exibirFormularioCadastro() {
        return "estoque/entrada";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto, RedirectAttributes attributes) {
        produtoService.salvarProduto(produto);
        attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
        return "redirect:/produtos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Produto produto = produtoService.buscarPorId(id);
        attributes.addFlashAttribute("mensagemErro", "Produto não encontrado.");
        return "redirect:/produtos";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            produtoService.excluirProdutoPorId(id);
            attributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", "Erro ao excluir produto: " + e.getMessage());
        }
        return "redirect:/produtos";
    }

    @GetMapping("/{id}/estoque")
    public String verEstoque(@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Produto produto = produtoService.buscarPorId(id);
        model.addAttribute("produto", produto);
        model.addAttribute("itensEstoque", estoqueService.listarPorProduto(id));
        model.addAttribute("totalEstoque", estoqueService.getTotalQuantidadePorProduto(id));
        return "estoque/lista-por-produto";

    }

}

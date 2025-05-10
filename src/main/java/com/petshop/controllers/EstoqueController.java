package com.petshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.model.Animal;
import com.petshop.model.Estoque;
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
        model.addAttribute("estoques", estoqueService.buscarEstoqueDeProdutos());
        return "estoque/lista";
    }

    @GetMapping("/entrada")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
        return "estoque/entrada";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Estoque estoque, @RequestParam("produtoId") Integer produtoId, RedirectAttributes attributes) {
        Produto produto = produtoService.buscarPorId(produtoId);
        estoque.setProduto(produto);
        estoqueService.salvar(estoque);
        attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
        return "redirect:/estoque";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Estoque estoque = estoqueService.buscarProdutoPorId(id);
        model.addAttribute("estoque", estoque);
        return "estoque/editar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            produtoService.excluirProdutoPorId(id);
            attributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", "Erro ao excluir produto: " + e.getMessage());
        }
        return "redirect:/estoque";
    }

    @GetMapping("/estoque/{id}")
    public String verEstoque(@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Produto produto = produtoService.buscarPorId(id);
        model.addAttribute("produto", produto);
        model.addAttribute("itensEstoque", estoqueService.listarPorProduto(id));
        model.addAttribute("totalEstoque", estoqueService.getTotalQuantidadePorProduto(id));
        return "estoque/lista-por-produto";

    }

}

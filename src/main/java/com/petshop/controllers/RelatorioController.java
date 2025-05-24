package com.petshop.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.petshop.repository.ProdutoRepository;
import com.petshop.services.RelatorioService;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping
    public String listarRelatorios(Model model) {
        return "relatorios/lista";
    }

    @GetMapping("/ranking-produtos")
    public String mostrarRanking(Model model) {
        List<Object[]> rankingData = produtoRepository.rankingProdutosMaisVendidos();
        model.addAttribute("rankingProdutos", rankingData);
        return "ranking-produtos";
    }

    @GetMapping("/estoque-vendas")
    public String relatorioEstoqueVendas(Model model) {
        Map<String, Map<String, Integer>> relatorio = null; //relatorioService.generarRelatorioEstoqueVendas();
        model.addAttribute("relatorio", relatorio);
        return "relatorioEstoqueVendas"; // Nome do arquivo HTML (Thymeleaf)
    }

    @GetMapping("/ranking-vendedores")
    public String mostrarRankingVendedores(Model model) {
        // Pega a lista de Object[] diretamente
        List<Object[]> ranking = relatorioService.getRankingVendedoresRaw();

        // Adiciona a lista ao modelo com um nome (ex: "rankingVendedores")
        model.addAttribute("rankingVendedores", ranking);

        // Retorna o nome do template Thymeleaf a ser renderizado
        return "/relatorios/ranking-vendedores"; // Supondo que você tenha um arquivo ranking-vendedores.html
    }

}

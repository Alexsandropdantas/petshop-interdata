package com.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.petshop.repository.ProdutoRepository;

@Controller
public class RelatorioController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/ranking-produtos")
    public String mostrarRanking(Model model) {
        List<Object[]> rankingData = produtoRepository.rankingProdutosMaisVendidos();
        model.addAttribute("rankingProdutos", rankingData);
        return "ranking-produtos"; // Nome do seu template Thymeleaf
    }

}

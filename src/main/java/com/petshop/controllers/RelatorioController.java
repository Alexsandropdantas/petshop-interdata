package com.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.petshop.repository.ProdutoRepository;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private ProdutoRepository produtoRepository;


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

}

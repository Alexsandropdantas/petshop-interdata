package com.petshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.petshop.services.ProdutoService;



@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
        return "index";
    }
    

}

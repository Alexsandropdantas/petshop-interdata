package com.petshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



import com.petshop.model.Categoria;
import com.petshop.services.CategoriaService;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;
    
    

    @GetMapping("/categorias")
    public String listarCategoria(Model model) {
        model.addAttribute("categorias", categoriaService.buscarTodosAsCategorias());
        return "categorias/lista";
    }

    @GetMapping("/categorias/cadastro")
    public String exibirFormularioCadastro() {
        return "categorias/cadastro";
    }


    @GetMapping("/categorias/editar/{id}")
    public String editarCategoria(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID categoria inválido: " + id));
        model.addAttribute("categoria", categoria);
        return "categorias/editar";
    }

    @PostMapping("/categorias/editar/{id}")
    public String atualizarCategoria(@PathVariable Long id, @ModelAttribute Categoria categoriaAtualizado) {
        Categoria categoria = categoriaService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID categoria inválido: " + id));
        categoria.setId(categoriaAtualizado.getId());
       categoria.setNome(categoriaAtualizado.getNome());
        categoriaService.salvarCategoria(categoria);
        return "redirect:/categorias";
    } 

    @GetMapping("/categorias/deletar/{id}")
    public String deletarcategoria(@PathVariable Long id) {
       categoriaService.excluirCategoriaPorId(id);
        return "redirect:/categorias";
    }

    // Podemos salvar sem a foto por isso verificamos se a foto veio vazia com o método isEmpty()
    @PostMapping("/categorias")
    public String salvarcategoria(@ModelAttribute Categoria categoria) {
        categoriaService.salvarCategoria(categoria);
        return "redirect:/categorias";
    }

}

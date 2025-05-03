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

import com.petshop.model.Vendedor;
import com.petshop.services.VendedorService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @GetMapping
    public String listarVendedores(Model model) {
        model.addAttribute("vendedores", vendedorService.buscarTodosOsVendedores());
        return "vendedores/lista";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("vendedor", new Vendedor());
        return "vendedores/cadastro";
    }

    @PostMapping
    public String salvarVendedor(@ModelAttribute Vendedor vendedor, RedirectAttributes redirectAttributes) {
        try {
            vendedorService.salvarVendedor(vendedor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Vendedor salvo com sucesso!");
            return "redirect:/vendedores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar vendedor: " + e.getMessage());
            return "vendedores/cadastro";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Vendedor vendedor = vendedorService.buscarPorIdOuFalhar(id);
            model.addAttribute("vendedor", vendedor);
            return "vendedores/editar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Vendedor não encontrado com ID: " + id);
            return "redirect:/vendedores";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizarVendedor(@PathVariable Integer id, @ModelAttribute Vendedor vendedor,
            RedirectAttributes redirectAttributes) {
        try {
            vendedor.setId(id);
            vendedorService.salvarVendedor(vendedor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Vendedor atualizado com sucesso!");
            return "redirect:/vendedores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar vendedor: " + e.getMessage());
            return "redirect:/vendedores/editar/" + id;
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarVendedor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            vendedorService.excluirVendedorPorId(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Vendedor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir vendedor: " + e.getMessage());
        }
        return "redirect:/vendedores";
    }
}
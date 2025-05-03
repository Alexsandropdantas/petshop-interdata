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

import com.petshop.model.Especie;
import com.petshop.services.EspecieService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/especies")
public class EspecieController {

    @Autowired
    private EspecieService especieService;

    @GetMapping
    public String listarEspecies(Model model) {
        model.addAttribute("especies", especieService.buscarTodasAsEspecies());
        return "especies/lista";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro() {
        return "especies/cadastro";
    }

    @PostMapping
    public String salvarEspecie(@ModelAttribute Especie especie, RedirectAttributes redirectAttributes) {
        try {
            especieService.salvarEspecie(especie);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Espécie salva com sucesso!");
            return "redirect:/especies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar espécie: " + e.getMessage());
            return "especies/cadastro";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Especie especie = especieService.buscarPorIdOuFalhar(id);
            model.addAttribute("especie", especie);
            return "especies/editar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Espécie não encontrada com ID: " + id);
            return "redirect:/especies";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizarEspecie(@PathVariable Integer id, @ModelAttribute Especie especie,
            RedirectAttributes redirectAttributes) {
        try {
            especie.setId(id);
            especieService.salvarEspecie(especie);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Espécie atualizada com sucesso!");
            return "redirect:/especies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar espécie: " + e.getMessage());
            return "redirect:/especies/editar/" + id;
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarEspecie(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            especieService.excluirEspeciePorId(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Espécie excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir espécie: " + e.getMessage());
        }
        return "redirect:/especies";
    }
}
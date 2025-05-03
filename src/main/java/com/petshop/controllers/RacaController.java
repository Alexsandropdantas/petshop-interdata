package com.petshop.controllers;

import com.petshop.model.Especie;
import com.petshop.model.Raca;
import com.petshop.services.EspecieService;
import com.petshop.services.RacaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/racas")
public class RacaController {

    @Autowired
    private RacaService racaService;
    @Autowired
    private EspecieService especieService;

    @GetMapping
    public String listarRacas(Model model) {
        model.addAttribute("racas", racaService.buscarTodasAsRacas());
        return "racas/lista";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("raca", new Raca());
        model.addAttribute("especies", especieService.buscarTodasAsEspecies());
        return "racas/cadastro";
    }

    @PostMapping
    public String salvarRaca(@ModelAttribute Raca raca, @RequestParam("especieId") Integer especieId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Especie especie = especieService.buscarPorIdOuFalhar(especieId);
            raca.setEspecie(especie);
            racaService.salvarRaca(raca);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Raça salva com sucesso!");
            return "redirect:/racas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar raça: " + e.getMessage());
            model.addAttribute("raca", raca);
            model.addAttribute("especies", especieService.buscarTodasAsEspecies());
            return "racas/cadastro";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Raca raca = racaService.buscarPorIdOuFalhar(id);
            model.addAttribute("raca", raca);
            model.addAttribute("especies", especieService.buscarTodasAsEspecies());
            return "racas/editar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Raça não encontrada com ID: " + id);
            return "redirect:/racas";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizarRaca(@PathVariable Integer id, @ModelAttribute Raca raca,
            @RequestParam("especieId") Integer especieId, RedirectAttributes redirectAttributes) {
        try {
            raca.setId(id);
            Especie especie = especieService.buscarPorIdOuFalhar(especieId);
            raca.setEspecie(especie);
            racaService.salvarRaca(raca);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Raça atualizada com sucesso!");
            return "redirect:/racas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar raça: " + e.getMessage());
            return "redirect:/racas/editar/" + id;
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarRaca(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            racaService.excluirRacaPorId(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Raça excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir raça: " + e.getMessage());
        }
        return "redirect:/racas";
    }
}
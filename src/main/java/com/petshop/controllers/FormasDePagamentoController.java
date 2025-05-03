package com.petshop.controllers;

import com.petshop.model.FormaDePagamento;
import com.petshop.services.FormasDePagamentoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/formasdepagamento")
public class FormasDePagamentoController {

    @Autowired
    private FormasDePagamentoService formasDePagamentoService;

    @GetMapping
    public String listarFormas(Model model) {
        model.addAttribute("formasdepagamento", formasDePagamentoService.listarTodas());
        return "formasdepagamento/lista";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro() {
        return "formasdepagamento/cadastro";
    }

    @PostMapping
    public String salvarFormaDePagamento(@ModelAttribute FormaDePagamento forma,
            RedirectAttributes redirectAttributes) {
        try {
            formasDePagamentoService.salvar(forma);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Forma de Pagamento salva com sucesso!");
            return "redirect:/formasdepagamento";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro ao salvar Forma de Pagamento: " + e.getMessage());
            return "formasdepagamento/cadastro";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            FormaDePagamento forma = formasDePagamentoService.buscarPorId(id);
            model.addAttribute("formadepagamento", forma);
            return "formasdepagamento/editar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Forma de Pagamento não encontrada com ID: " + id);
            return "redirect:/formasdepagamento";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizarForma(@PathVariable Integer id, @ModelAttribute FormaDePagamento forma,
            RedirectAttributes redirectAttributes) {
        try {
            forma.setId(id);
            formasDePagamentoService.salvar(forma);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Forma de Pagamento atualizada com sucesso!");
            return "redirect:/formasdepagamento";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro ao atualizar Forma de Pagamento: " + e.getMessage());
            return "redirect:/formasdepagamento/editar/" + id;
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarForma(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            formasDePagamentoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Forma de Pagamento excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro ao excluir Forma de Pagamento: " + e.getMessage());
        }
        return "redirect:/formasdepagamento";
    }
}
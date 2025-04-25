package com.petshop.controllers;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.petshop.model.Pedido;
import com.petshop.model.Raca;
import com.petshop.services.RacaService;

@Controller
public class RacaController {

    @Autowired
    private RacaService racaService;
    
   

    @GetMapping("/racas")
    public String listarRaca(Model model) {
        model.addAttribute("racas", racaService.buscarTodasAsRacas());
        return "racas/lista";
    }

    @GetMapping("/racas/cadastro")
    public String exibirFormularioCadastro() {
        return "racas/cadastro";
    }


    @GetMapping("/racas/editar/{id}")
    public String editarRaca(@PathVariable Long id, Model model) {
        Raca raca = racaService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID raça inválido: " + id));
        model.addAttribute("racas", raca);
        return "racas/editar";
    }

    @PostMapping("/racas/editar/{id}")
    public String atualizarRaca(@PathVariable Long id, @ModelAttribute Raca racaAtualizado) {
       Raca raca = racaService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID raça inválido: " + id));
       raca.setId(racaAtualizado.getId());
        raca.setNome(racaAtualizado.getNome());
        racaService.salvarRaca(raca);
        return "redirect:/racas";
    }

    @GetMapping("/racas/deletar/{id}")
    public String deletarraca(@PathVariable Long id) {
        racaService.excluirRacaPorId(id);
        return "redirect:/racas";
    }
 @PostMapping("/racas")
    public String salvarracas(@ModelAttribute Raca raca) {
        racaService.salvarRaca(raca);
        return "redirect:/racas";
    }
   

}

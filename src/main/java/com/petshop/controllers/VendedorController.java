package com.petshop.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.petshop.model.Vendedor;
import com.petshop.services.VendedorService; 

@Controller
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @Value("${imagens.vendedores.path}")
    private String imagesPath;

    @GetMapping("/vendedores")
    public String listarVendedores(Model model) {
        model.addAttribute("vendedores", vendedorService.buscarTodosOsVendedores()); // Corrigido o uso de vendedorService
        return "vendedores/lista";
    }

    @GetMapping("/vendedores/cadastro")
    public String exibirFormularioCadastro() {
        return "vendedores/cadastro";
    }

    @GetMapping("/vendedores/editar/{id}")
    public String editarVendedor(@PathVariable Long id, Model model) {
        Vendedor vendedor = vendedorService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID vendedor inválido: " + id));
        model.addAttribute("vendedor", vendedor);
        return "vendedores/editar"; // Corrigido a rota para "vendedores/editar"
    }

    @PostMapping("/vendedores/editar/{id}")
    public String atualizarVendedor(@PathVariable Long id, @ModelAttribute Vendedor vendedorAtualizado) {
        Vendedor vendedor = vendedorService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID vendedor inválido: " + id));
        vendedor.setNome(vendedorAtualizado.getNome());
        vendedor.setEmail(vendedorAtualizado.getEmail());
        vendedor.setTelefone(vendedorAtualizado.getTelefone());
        vendedor.setEndereco(vendedorAtualizado.getEndereco());
        vendedorService.salvarVendedor(vendedor); // Corrigido método de salvar
        return "redirect:/vendedores";
    }

    @GetMapping("/vendedores/deletar/{id}")
    public String deletarVendedor(@PathVariable Long id) {
        vendedorService.excluirVendedorPorId(id);
        return "redirect:/vendedores";
    }

    @PostMapping("/vendedores")
    public String salvarVendedor(@ModelAttribute Vendedor vendedor, @RequestParam("foto") MultipartFile foto) throws IOException {
        if (!foto.isEmpty()) {
            String nomeArquivo = foto.getOriginalFilename();  // adicionar uma chave tipo data e hora
            Path caminho = Paths.get(imagesPath + nomeArquivo);
            Files.copy(foto.getInputStream(), caminho);
            vendedor.setFotoPath(caminho.toString());
        }
        vendedorService.salvarVendedor(vendedor); // Corrigido método de salvar
        return "redirect:/vendedores";
    }
}

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

import com.petshop.model.Cliente;

import com.petshop.services.ClienteService;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Value("${imagens.clientes.path}")
    private String imagesPath;

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.buscarTodosOsClientes());
        return "clientes/lista";
    }

    @GetMapping("/clientes/cadastro")
    public String exibirFormularioCadastro() {
        return "clientes/cadastro";
    }
    
   @GetMapping("/clientes/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID cliente inválido: " + id));
        model.addAttribute("cliente", cliente);
        return "clientes/editar";
    }

    @PostMapping("/clientes/editar/{id}")
    public String atualizarCliente(@PathVariable Long id, @ModelAttribute Cliente clienteAtualizado) {
        Cliente cliente = clienteService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID cliente inválido: " + id));
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setEndereco(clienteAtualizado.getEndereco());
        clienteService.salvarCliente(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/deletar/{id}")
    public String deletarCliente(@PathVariable Long id) {
        clienteService.excluirClientePorId(id);
        return "redirect:/clientes";
    }

    
    // Podemos salvar sem a foto por isso verificamos se a foto veio vazia com o método isEmpty()
    @PostMapping("/clientes")
    public String salvarCliente(@ModelAttribute Cliente cliente, @RequestParam("foto") MultipartFile foto) throws IOException {
        if (!foto.isEmpty()) {
            String nomeArquivo = foto.getOriginalFilename();  // adicionar uma chave tipo data e hora
            Path caminho = Paths.get(imagesPath + nomeArquivo);
            Files.copy(foto.getInputStream(), caminho);
            cliente.setFotoPath(caminho.toString());
        }
        clienteService.salvarCliente(cliente);
        return "redirect:/clientes";
    }
}

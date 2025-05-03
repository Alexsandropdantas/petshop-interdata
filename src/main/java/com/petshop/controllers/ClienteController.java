package com.petshop.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.comum.BibliotecaDeMetodosComunsAoSistema;
import com.petshop.model.Cliente;
import com.petshop.services.ClienteService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/clientes") // Adicionar RequestMapping a nível de classe
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Value("${imagens.clientes.path}")
    private String imagesPath;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.buscarTodosOsClientes());
        return "clientes/lista";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) { // Adicionar Model
        model.addAttribute("cliente", new Cliente()); // Adicionar objeto vazio ao Model
        return "clientes/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorId(id);
            model.addAttribute("cliente", cliente);
            return "clientes/editar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Cliente não encontrado com ID: " + id);
            return "redirect:/clientes";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizarCliente(@PathVariable Integer id,
            @ModelAttribute Cliente clienteAtualizado,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            clienteAtualizado.setId(id);

            // Lida com a foto apenas se uma nova foi enviada
            if (foto != null && !foto.isEmpty()) {
                String nomeArquivo = System.currentTimeMillis() + "_"
                        + foto.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
                Path diretorioPath = Paths.get(imagesPath);
                Files.createDirectories(diretorioPath);
                Path caminhoArquivo = diretorioPath.resolve(nomeArquivo);
                Files.copy(foto.getInputStream(), caminhoArquivo);
                clienteAtualizado.setFotoPath(caminhoArquivo.toString());
            } else {
                // Mantém a foto existente se nenhuma nova foi enviada
                // O service buscará o existente e manterá o fotoPath se o novo for null/vazio
                Cliente clienteExistente = clienteService.buscarPorId(id);
                clienteAtualizado.setFotoPath(clienteExistente.getFotoPath()); // Garante que não se perca
            }

            // O service salvarCliente fará a lógica de merge/update
            clienteService.salvarCliente(clienteAtualizado);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cliente atualizado com sucesso!");
            return "redirect:/clientes";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar a foto do cliente.");
            // Logar erro
            model.addAttribute("cliente", clienteAtualizado);
            return "clientes/editar"; // Volta para o form de edição
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar cliente: " + e.getMessage());
            // Logar erro
            return "redirect:/clientes/editar/" + id; // Volta para o form de edição
        }
    }

    @PostMapping // Salvar no POST /clientes
    public String salvarCliente(@ModelAttribute Cliente cliente,
            @RequestParam("foto") MultipartFile foto,
            RedirectAttributes redirectAttributes) { // Usar RedirectAttributes
        try {
            if (!foto.isEmpty()) {
                String nomeUUID = UUID.randomUUID().toString().replace("-", "");
                Path diretorioPath = Paths.get(imagesPath);
                Files.createDirectories(diretorioPath);
                Path caminhoArquivo = diretorioPath.resolve(nomeUUID);
                Files.copy(foto.getInputStream(), caminhoArquivo);

                // Salva o caminho no banco removendo src\main\resources\static\
                cliente.setFotoPath(BibliotecaDeMetodosComunsAoSistema.caminhoDasImagensWeb(caminhoArquivo.toString()));
            }
            // O service fará a lógica de criar novo
            clienteService.salvarCliente(cliente);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cliente salvo com sucesso!");
            return "redirect:/clientes";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar a foto do cliente.");
            // Logar erro
            // model.addAttribute("cliente", cliente); // Devolve o objeto com erro para o
            // form
            return "clientes/cadastro"; // Volta para o form de cadastro
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar cliente: " + e.getMessage());
            // Logar erro
            // model.addAttribute("cliente", cliente);
            return "clientes/cadastro";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) { // Usar Integer
        try {
            // Opcional: Excluir foto
            clienteService.excluirClientePorId(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cliente excluído com sucesso!");
        } catch (Exception e) { // Captura EntityNotFound e outras
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir cliente: " + e.getMessage());
            // Logar erro
        }
        return "redirect:/clientes";
    }
}
package com.petshop.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;
import java.nio.file.Path;
import com.petshop.services.RacaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.petshop.comum.BibliotecaDeMetodosComunsAoSistema;
import com.petshop.model.Animal;
import com.petshop.model.Cliente;
import com.petshop.model.Raca;
import com.petshop.services.AnimalService;
import com.petshop.services.ClienteService;

@Controller
@RequestMapping("/animais")
public class AnimalController {

    private final RacaService racaService;

    @Autowired
    private AnimalService animalService; // Injetando AnimalService

    @Autowired
    private ClienteService clienteService; // Injetando ClienteService

    @Value("${imagens.animais.path}") // Caminho para salvar as imagens cadastrado em application.properties
    private String imagesPath;

    AnimalController(RacaService racaService) {
        this.racaService = racaService;
    }

    @GetMapping
    public String listarAnimais(Model model) {
        model.addAttribute("animais", animalService.buscarTodosOsAnimais());
        return "animais/lista";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("animal", new Animal());
        model.addAttribute("clientes", clienteService.buscarTodosOsClientes()); // Lista de clientes para seleção
        model.addAttribute("racas", racaService.buscarTodasAsRacas()); // Lista todas as raças cadastradas
        return "animais/cadastro";
    }

    @PostMapping
    public String salvarAnimal(@ModelAttribute Animal animal,
            @RequestParam("racaId") Integer racaId,
            @RequestParam("foto") MultipartFile foto,
            @RequestParam("clienteId") Integer clienteId,
            @RequestParam("dataDeNascimento") LocalDateTime dataDeNascimento) throws IOException {

        Cliente cliente = clienteService.buscarPorId(clienteId);
        animal.setCliente(cliente); // Seta o Cliente na classe Animal

        Raca raca = racaService.buscarPorId(racaId)
                .orElseThrow(() -> new IllegalArgumentException("Raca inválida: " + racaId));
        animal.setRaca(raca); // Seta a Raça na classe Animal

        animal.setDataDeNascimento(dataDeNascimento);

        if (!foto.isEmpty()) {
            String nomeUUID = UUID.randomUUID().toString().replace("-", "");
            Path diretorioPath = Paths.get(imagesPath);
            Files.createDirectories(diretorioPath);
            Path caminhoArquivo = diretorioPath.resolve(nomeUUID);
            Files.copy(foto.getInputStream(), caminhoArquivo);

            // Salva o caminho no banco de dados removendo src\main\resources\static\
            animal.setFotoPath(BibliotecaDeMetodosComunsAoSistema.caminhoDasImagensWeb(caminhoArquivo.toString()));
        }

        animalService.salvarAnimal(animal);
        return "redirect:/animais";
    }

    @GetMapping("/editar/{id}")
    public String editarAnimal(@PathVariable Integer id, Model model) {
        Animal animal = animalService.buscarPorId(id);

        model.addAttribute("animal", animal);
        model.addAttribute("clientes", clienteService.buscarTodosOsClientes()); // Permite alterar o cliente
        return "animais/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarAnimal(@PathVariable Integer id,
            @ModelAttribute Animal animalAtualizado,
            @RequestParam("clienteId") Integer clienteId) {
        Animal animal = animalService.buscarPorId(id);

        Cliente cliente = clienteService.buscarPorId(clienteId);

        animal.setNome(animalAtualizado.getNome());
        animal.setRaca(animalAtualizado.getRaca());
        animal.setDataDeNascimento(animalAtualizado.getDataDeNascimento());
        animal.setCliente(cliente); // Atualiza o cliente do animal

        animalService.salvarAnimal(animal);
        return "redirect:/animais";
    }

    @GetMapping("/deletar/{id}")
    public String deletarAnimal(@PathVariable Integer id) {
        animalService.excluirAnimalPorId(id);
        return "redirect:/animais";
    }
}

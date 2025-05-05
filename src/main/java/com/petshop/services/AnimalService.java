package com.petshop.services;

import com.petshop.model.Animal;
import com.petshop.model.Cliente;
import com.petshop.model.Raca;
import com.petshop.repository.AnimalRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ClienteService clienteService;
    private final RacaService racaService;

    public AnimalService(AnimalRepository animalRepository, ClienteService clienteService, RacaService racaService) {
        this.animalRepository = animalRepository;
        this.clienteService = clienteService;
        this.racaService = racaService;
    }

    public List<Animal> buscarTodosOsAnimais() {
        return animalRepository.findAll();
    }

    public Animal buscarPorId(Integer id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado com ID: " + id));
    }

    public Animal salvarAnimal(Animal animal) {
        if (animal.getCliente() != null && animal.getCliente().getId() != null) {
            Cliente cliente = clienteService.buscarPorId(animal.getCliente().getId());
            animal.setCliente(cliente);
        } else {
            throw new IllegalArgumentException("Cliente é obrigatório para salvar o animal.");
        }

        if (animal.getRaca() != null && animal.getRaca().getId() != null) {
            Raca raca = racaService.buscarPorId(animal.getRaca().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Raça não encontrada para este animal"));
            animal.setRaca(raca);
        } else {
            throw new IllegalArgumentException("Raça é obrigatória para salvar o animal.");
        }

        // Lógica para atualização e criação de novo animal
        if (animal.getId() != null) {
            Animal existente = buscarPorId(animal.getId());
            existente.setNome(animal.getNome());
            existente.setDataDeNascimento(animal.getDataDeNascimento());
            existente.setCliente(animal.getCliente());
            existente.setRaca(animal.getRaca());
            if (animal.getFotoPath() != null && !animal.getFotoPath().isBlank()) {
                existente.setFotoPath(animal.getFotoPath());
            }
            return animalRepository.save(existente);
        } else {
            return animalRepository.save(animal);
        }
    }

    public void excluirAnimalPorId(Integer id) {
        animalRepository.deleteById(id);
    }
}

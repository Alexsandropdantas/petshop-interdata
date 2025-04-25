package com.petshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.Animal;
import com.petshop.repository.AnimalRepository;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    // Listar todos os animais
    public List<Animal> buscarTodosOsAnimais() {
        return animalRepository.findAll();
    }

    public void salvarAnimal(Animal animal) {
        animalRepository.save(animal);
    }

    // Buscar um animal por ID
    public Optional<Animal> buscarPorId(Long id) {
        return animalRepository.findById(id);
    }

    // Deletar um animal
    public void excluirAnimalPorId(Long id) {
        animalRepository.deleteById(id);
    }

    // Editar animal (atualizar suas informações)
    public Animal atualizarAnimal(Animal animal) {
        if (animal.getId() != null) {
            return animalRepository.save(animal);  // aqui chamamos o método save acima
        }
        return null;
    }


}
package com.petshop.services;

import com.petshop.model.Especie;
import com.petshop.repository.EspecieRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecieService {

    private final EspecieRepository especieRepository;

    @Autowired
    public EspecieService(EspecieRepository especieRepository) {
        this.especieRepository = especieRepository;
    }

    public List<Especie> buscarTodasAsEspecies() {
        return especieRepository.findAll();
    }

    public Optional<Especie> buscarPorId(Integer id) {
        return especieRepository.findById(id);
    }

    public Especie buscarPorIdOuFalhar(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Espécie não encontrada com ID: " + id));
    }

    public Especie salvarEspecie(Especie especie) {
        if (especie.getId() != null) {
            Especie existente = buscarPorIdOuFalhar(especie.getId());
            existente.setNome(especie.getNome());
            return especieRepository.save(existente);
        } else {
            return especieRepository.save(especie);
        }
    }

    public void excluirEspeciePorId(Integer id) {
        if (!especieRepository.existsById(id)) {
            throw new EntityNotFoundException("Espécie não encontrada com ID: " + id);
        }
        especieRepository.deleteById(id);
    }
}
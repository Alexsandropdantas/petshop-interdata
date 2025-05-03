package com.petshop.services;

import com.petshop.model.Especie;
import com.petshop.model.Raca;
import com.petshop.repository.RacaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RacaService {

    private final RacaRepository racaRepository;
    private final EspecieService especieService; // Para buscar especie

    public RacaService(RacaRepository racaRepository, @Lazy EspecieService especieService) {
        this.racaRepository = racaRepository;
        this.especieService = especieService;
    }

    public List<Raca> buscarTodasAsRacas() {
        return racaRepository.findAll();
    }

    public Optional<Raca> buscarPorId(Integer id) {
        return racaRepository.findById(id);
    }

     public Raca buscarPorIdOuFalhar(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Raça não encontrada com ID: " + id));
    }

    public Raca salvarRaca(Raca raca) {
        // Validar e anexar Especie
        if (raca.getEspecie() == null || raca.getEspecie().getId() == null) {
            throw new IllegalArgumentException("Espécie é obrigatória para a raça.");
        }
        Especie especie = especieService.buscarPorIdOuFalhar(raca.getEspecie().getId());
        raca.setEspecie(especie);

        if (raca.getId() != null) {
            Raca existente = buscarPorIdOuFalhar(raca.getId());
            existente.setNome(raca.getNome());
            existente.setEspecie(raca.getEspecie()); // Atualiza especie
            return racaRepository.save(existente);
        } else {
            return racaRepository.save(raca);
        }
    }

    public void excluirRacaPorId(Integer id) {
        if (!racaRepository.existsById(id)) {
            throw new EntityNotFoundException("Raça não encontrada com ID: " + id);
        }
        // Adicionar verificação de dependência (Animais) se necessário
        racaRepository.deleteById(id);
    }
}
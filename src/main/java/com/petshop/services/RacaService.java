package com.petshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.Raca;
import com.petshop.repository.RacaRepository;

@Service
public class RacaService {

    @Autowired
    private RacaRepository racaRepository;

    
    public List<Raca> buscarTodasAsRacas() {
        return racaRepository.findAll();
    }

   
    public void salvarRaca(Raca raca) {
        racaRepository.save(raca); 
    }

 
    public Optional<Raca> buscarPorId(Long id) {
        return racaRepository.findById(id);
    }

  
    public void excluirRacaPorId(Long id) {
        racaRepository.deleteById(id);
    }

   
    public Raca atualizarRaca(Raca raca) {
        if (raca.getId() != null) {
            return racaRepository.save(raca);  
        }
        return null;
    }


}
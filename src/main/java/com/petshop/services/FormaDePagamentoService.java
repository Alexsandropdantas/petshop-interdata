package com.petshop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.model.FormaDePagamento;
import com.petshop.repository.FormaDePagamentoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FormaDePagamentoService {

    private final FormaDePagamentoRepository formaDePagamentoRepository;

    @Autowired
    public FormaDePagamentoService(FormaDePagamentoRepository formaDePagamentoRepository) {
        this.formaDePagamentoRepository = formaDePagamentoRepository;
    }

    public List<FormaDePagamento> listarTodas() {
        return formaDePagamentoRepository.findAll();
    }

    public FormaDePagamento buscarPorId(Integer id) {
        return formaDePagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não encontrada com ID: " + id));
    }

    public FormaDePagamento salvar(FormaDePagamento formaPagamento) {
        return formaDePagamentoRepository.save(formaPagamento);
    }

    public FormaDePagamento atualizar(Integer id, FormaDePagamento formaPagamentoAtualizada) {
        if (!formaDePagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Forma de pagamento não encontrada com ID: " + id);
        }
        
        formaPagamentoAtualizada.setId(id);
        return formaDePagamentoRepository.save(formaPagamentoAtualizada);
    }

    public void excluir(Integer id) {
        if (!formaDePagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Forma de pagamento não encontrada com ID: " + id);
        }
        
        formaDePagamentoRepository.deleteById(id);
    }

    public FormaDePagamento buscarPorDescricao(String descricao) {
        return formaDePagamentoRepository.findByDescricao(descricao)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não encontrada com descrição: " + descricao));
    }
}
package com.petshop.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petshop.model.FormaDePagamento;
import com.petshop.repository.FormasDePagamentoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FormasDePagamentoService {

    private final FormasDePagamentoRepository formasDePagamentoRepository;

    public FormasDePagamentoService(FormasDePagamentoRepository formaDePagamentoRepository) {
        this.formasDePagamentoRepository = formaDePagamentoRepository;
    }

    public List<FormaDePagamento> listarTodas() {
        return formasDePagamentoRepository.findAll();
    }

    public FormaDePagamento buscarPorId(Integer id) {
        return formasDePagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não encontrada com ID: " + id));
    }

    public FormaDePagamento salvar(FormaDePagamento formaPagamento) {
        return formasDePagamentoRepository.save(formaPagamento);
    }

    public FormaDePagamento atualizar(Integer id, FormaDePagamento formaPagamentoAtualizada) {
        if (!formasDePagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Forma de pagamento não encontrada com ID: " + id);
        }

        formaPagamentoAtualizada.setId(id);
        return formasDePagamentoRepository.save(formaPagamentoAtualizada);
    }

    public void excluir(Integer id) {
        if (!formasDePagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Forma de pagamento não encontrada com ID: " + id);
        }

        formasDePagamentoRepository.deleteById(id);
    }

    public FormaDePagamento buscarPorDescricao(String descricao) {
        return formasDePagamentoRepository.findByDescricao(descricao)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Forma de pagamento não encontrada com descrição: " + descricao));
    }
}
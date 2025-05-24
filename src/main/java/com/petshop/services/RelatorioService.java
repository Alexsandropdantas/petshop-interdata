package com.petshop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshop.repository.EstoqueRepository;
import com.petshop.repository.ItemDePedidoRepository;
import com.petshop.repository.ProdutoRepository;
import com.petshop.repository.VendedorRepository;

@Service
public class RelatorioService {

    @Autowired
    private VendedorRepository vendedorRepository;
    // Este método agora retorna diretamente a lista de Object[] do banco
    public List<Object[]> getRankingVendedoresRaw() {
        return vendedorRepository.findVendedoresTotalVendido();
    }
}
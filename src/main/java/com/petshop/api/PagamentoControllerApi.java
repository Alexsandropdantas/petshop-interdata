package com.petshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petshop.services.PagamentoService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vendas")
public class PagamentoControllerApi {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/2024")
    public ResponseEntity<List<Map<String, Object>>> getVendasPorMes2024() {

        List<Map<String, Object>> vendas = pagamentoService.getTotalVendasPorMes2024();

        return ResponseEntity.ok(vendas);
    }
}
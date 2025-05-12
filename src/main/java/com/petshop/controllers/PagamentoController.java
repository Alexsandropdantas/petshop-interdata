package com.petshop.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.model.FormaDePagamento;
import com.petshop.model.ItemDePedido;
import com.petshop.model.Pagamento;
import com.petshop.model.Pedido;
import com.petshop.services.FormasDePagamentoService;
import com.petshop.services.ItemDePedidoService;
import com.petshop.services.PagamentoService;
import com.petshop.services.PedidoService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final FormasDePagamentoService formasDePagamentoService;

    private final PedidoService pedidoService;
    private final ItemDePedidoService itemDePedidoService;


    public PagamentoController(PagamentoService pagamentoService,
            FormasDePagamentoService formasDePagamentoService,
            PedidoService pedidoService,
            ItemDePedidoService itemDePedidoService){
        this.pagamentoService = pagamentoService;
        this.pedidoService = pedidoService;
        this.formasDePagamentoService = formasDePagamentoService;
        this.itemDePedidoService = itemDePedidoService;
    }

    @GetMapping
    public String listarTodos(Model model) {
        List<Pagamento> pagamentos = pagamentoService.listarTodos();
        model.addAttribute("pagamentos", pagamentos);
        return "pagamentos/lista";
    }

    @GetMapping("/{id}") // Visualizar um pagamento específico
    public String visualizar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Pagamento pagamento = pagamentoService.buscarPorId(id);
            model.addAttribute("pagamento", pagamento);
            model.addAttribute("pedido", pagamento.getPedido());
            model.addAttribute("formaPagamento", pagamento.getFormaDePagamento());
            return "pagamentos/visualizar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Pagamento não encontrado com ID: " + id);
            return "redirect:/pagamentos";
        }
    }

    @GetMapping("/pedido/{numeroPedido}")
    public String exibirPaginaPagamento(@PathVariable("numeroPedido") Integer numeroPedido, Model model) {
        // Busca o pedido pelo número
        Pedido pedido = pedidoService.buscarPorId(numeroPedido);
        
        // Busca os itens do pedido
        List<ItemDePedido> itensDePedido = itemDePedidoService.buscarPorNumeroDoPedido(numeroPedido);
        
        // Calcula o valor total do pedido
        double totalPedido = itensDePedido.stream()
                .mapToDouble(item -> (item.getPrecoUnitario() * item.getQuantidade()) - (item.getDesconto() != null ? item.getDesconto() : 0.0))
                .sum();
        
        // Busca todos os pagamentos já realizados para este pedido
        List<Pagamento> pagamentos = pagamentoService.buscarPorPedido(numeroPedido);
        
        // Calcula o total já pago
        double totalPago = pagamentos.stream()
                .mapToDouble(pagamento -> pagamento.getValorPago() != null ? pagamento.getValorPago() : 0.0)
                .sum();
        
        // Busca todas as formas de pagamento disponíveis
        List<FormaDePagamento> formasDePagamento = formasDePagamentoService.listarTodas();
        
        // Prepara um objeto de pagamento vazio para o formulário
        Pagamento novoPagamento = new Pagamento();
        
        // Adiciona todos os objetos ao modelo
        model.addAttribute("pedido", pedido);
        model.addAttribute("itensDePedido", itensDePedido);
        model.addAttribute("pagamentos", pagamentos);
        model.addAttribute("formasDePagamento", formasDePagamento);
        model.addAttribute("totalPedido", totalPedido);
        model.addAttribute("totalPago", totalPago);
        model.addAttribute("pagamento", novoPagamento);
        
        return "pagamentos/pagamentopedido";
    }
    
    /**
     * Salva um novo pagamento
     */
    @PostMapping("/salvar")
    public String salvarPagamento(Pagamento pagamento, Integer fkFormasDePagamentoId, 
                                Integer fkPedidosNumeroPedido, RedirectAttributes redirectAttributes) {
        
        try {
            // Verifica se o pedido existe
            Pedido pedido = pedidoService.buscarPorId(fkPedidosNumeroPedido);
            
            // Verifica se a forma de pagamento existe
            FormaDePagamento formaPagamento = formasDePagamentoService.buscarPorId(fkFormasDePagamentoId);

            
            // Calcula o total já pago
            List<Pagamento> pagamentos = pagamentoService.buscarPorPedido(fkPedidosNumeroPedido);

            // pagamento.setFkPedidosNumeroPedido(pedido.get());
            // pagamento.setFkFormasDePagamento(formaPagamentoOpt.get());
            // pagamento.setDataEHora(LocalDateTime.now());
            
            // // Salva o pagamento
            // pagamentosRepository.save(pagamento);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento registrado com sucesso!");
            return "redirect:/pagamentos/pedido/" + fkPedidosNumeroPedido;
            
        } catch(Exception e){
            return "redirect:/pagamentos";
        }
    }
    
    /**
     * Exclui um pagamento
     */
    @GetMapping("/deletar/{id}")
    public String deletarPagamento(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Busca o pagamento
            pagamentoService.excluir(id);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento excluído com sucesso!");
            return "redirect:/pagamentos";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir pagamento: " + e.getMessage());
            return "redirect:/pedidos";
        }
    }
}
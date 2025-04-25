package com.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.model.FormaDePagamento;
import com.petshop.model.Pagamento;
import com.petshop.model.Pedido;
import com.petshop.services.FormaDePagamentoService;
import com.petshop.services.PagamentoService;
import com.petshop.services.PedidoService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final PedidoService pedidoService;
    private final FormaDePagamentoService formaDePagamentoService;

    @Autowired
    public PagamentoController(PagamentoService pagamentoService, 
                              PedidoService pedidoService,
                              FormaDePagamentoService formaPagamentoService) {
        this.pagamentoService = pagamentoService;
        this.pedidoService = pedidoService;
        this.formaDePagamentoService = formaPagamentoService;
    }

    @GetMapping
    public String listarTodos(Model model) {
        List<Pagamento> pagamentos = pagamentoService.listarTodos();
        model.addAttribute("pagamentos", pagamentos);
        return "pagamentos/lista";
    }

    @GetMapping("/{id}")
    public String visualizar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Pagamento pagamento = pagamentoService.buscarPorId(id);
            model.addAttribute("pagamento", pagamento);
            return "pagamentos/visualizar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Pagamento não encontrado com ID: " + id);
            return "redirect:/pagamentos";
        }
    }

    @GetMapping("/pedido/{numeroPedido}")
    public String listarPorPedido(@PathVariable Integer numeroPedido, Model model) {
        List<Pagamento> pagamentos = pagamentoService.buscarPorPedido(numeroPedido);
        Double totalPago = pagamentoService.calcularTotalPagamentoPorPedido(numeroPedido);
        
        model.addAttribute("pagamentos", pagamentos);
        model.addAttribute("numeroPedido", numeroPedido);
        model.addAttribute("totalPago", totalPago);
        
        return "pagamentos/lista-por-pedido";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("pagamento", new Pagamento());
        
        List<Pedido> pedidos = pedidoService.listarTodos();
        List<FormaDePagamento> formasPagamento = formaDePagamentoService.listarTodas();
        
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("formasPagamento", formasPagamento);
        
        return "pagamentos/form";
    }
    
    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Pagamento pagamento = pagamentoService.buscarPorId(id);
            model.addAttribute("pagamento", pagamento);
            
            List<Pedido> pedidos = pedidoService.listarTodos();
            List<FormaDePagamento> formasPagamento = formaDePagamentoService.listarTodas();
            
            model.addAttribute("pedidos", pedidos);
            model.addAttribute("formasPagamento", formasPagamento);
            
            return "pagamentos/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Pagamento não encontrado com ID: " + id);
            return "redirect:/pagamentos";
        }
    }
    
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Pagamento pagamento, RedirectAttributes redirectAttributes) {
        try {
            if (pagamento.getId() == null) {
                pagamentoService.salvar(pagamento);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento registrado com sucesso!");
            } else {
                pagamentoService.atualizar(pagamento.getId(), pagamento);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento atualizado com sucesso!");
            }
            return "redirect:/pagamentos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar pagamento: " + e.getMessage());
            return "redirect:/pagamentos/novo";
        }
    }
    
    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            pagamentoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento excluído com sucesso!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Pagamento não encontrado com ID: " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir pagamento: " + e.getMessage());
        }
        return "redirect:/pagamentos";
    }
    
    @GetMapping("/relatorio")
    public String relatorio(Model model) {
        List<Pagamento> todosOsPagamentos = pagamentoService.listarTodos();
        
        Double valorTotal = 0.0;
        for (Pagamento pagamento : todosOsPagamentos) {
            valorTotal += pagamento.getValorPago();
        }
        
        model.addAttribute("pagamentos", todosOsPagamentos);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("quantidadePagamentos", todosOsPagamentos.size());
        
        return "pagamentos/relatorio";
    }
}
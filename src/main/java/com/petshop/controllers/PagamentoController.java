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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.model.FormaDePagamento;
import com.petshop.model.Pagamento;
import com.petshop.model.Pedido;
import com.petshop.services.FormasDePagamentoService;
import com.petshop.services.PagamentoService;
import com.petshop.services.PedidoService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final PedidoService pedidoService;
    private final FormasDePagamentoService formaDePagamentoService;

    @Autowired
    public PagamentoController(PagamentoService pagamentoService,
            PedidoService pedidoService,
            FormasDePagamentoService formaPagamentoService) {
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
    public String listarPorPedido(@PathVariable Integer numeroPedido, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Pedido pedido = pedidoService.buscarPorId(numeroPedido); // Busca o pedido para exibir detalhes
            List<Pagamento> pagamentos = pagamentoService.buscarPorPedido(numeroPedido);
            Double totalPago = pagamentoService.calcularTotalPagamentoPorPedido(numeroPedido);
            Double totalPedido = 0.0;
            Double saldoDevedor = totalPedido - totalPago;

            model.addAttribute("pedido", pedido);
            model.addAttribute("pagamentos", pagamentos);
            model.addAttribute("numeroPedido", numeroPedido);
            model.addAttribute("totalPago", totalPago);
            model.addAttribute("totalPedido", totalPedido);
            model.addAttribute("saldoDevedor", saldoDevedor);

            // Adiciona um novo objeto Pagamento para o form de adicionar mais pagamento
            Pagamento novoPagamento = new Pagamento();
            novoPagamento.setPedido(pedido); // Pré-associa o pedido
            model.addAttribute("novoPagamento", novoPagamento);
            model.addAttribute("formasPagamento", formaDePagamentoService.listarTodas());

            return "pagamentos/lista-por-pedido"; // Criar/Ajustar view
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Pedido não encontrado com ID: " + numeroPedido);
            return "redirect:/pedidos";
        }
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Pagamento pagamento = pagamentoService.buscarPorId(id);
            model.addAttribute("pagamento", pagamento);
            model.addAttribute("pedidos", pedidoService.buscarTodosOsPedidos()); // Todos os pedidos para possível troca
            model.addAttribute("formasPagamento", formaDePagamentoService.listarTodas());
            model.addAttribute("pedidoSelecionadoId", pagamento.getPedido().getNumeroPedido()); // Marca o pedido atual
            model.addAttribute("formaSelecionadaId", pagamento.getFormaDePagamento().getId()); // Marca a forma atual

            return "pagamentos/form"; // Reutiliza o form
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Pagamento não encontrado com ID: " + id);
            return "redirect:/pagamentos";
        }
    }

    @PostMapping("/salvar") // Endpoint único para salvar (novo ou edição)
    public String salvar(@ModelAttribute Pagamento pagamento,
            @RequestParam("pedidoId") Integer pedidoId, // Pega ID do pedido explicitamente do form
            @RequestParam("formaPagamentoId") Integer formaPagamentoId, // Pega ID da forma explicitamente
            Model model,
            RedirectAttributes redirectAttributes) {
        String redirectUrl = "/pagamentos"; // Destino padrão
        try {
            // Associa Pedido e Forma de Pagamento pelos IDs recebidos
            Pedido pedido = pedidoService.buscarPorId(pedidoId);
            FormaDePagamento forma = formaDePagamentoService.buscarPorId(formaPagamentoId);
            pagamento.setPedido(pedido);
            pagamento.setFormaDePagamento(forma);

            if (pagamento.getId() == null) {
                // Novo pagamento
                pagamentoService.salvar(pagamento);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento registrado com sucesso!");
                redirectUrl = "/pagamentos/pedido/" + pedidoId; // Volta para a lista de pagamentos do pedido
            } else {
                // Atualização
                pagamentoService.atualizar(pagamento.getId(), pagamento);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento atualizado com sucesso!");
                redirectUrl = "/pagamentos/pedido/" + pedidoId; // Volta para a lista de pagamentos do pedido
            }
            return "redirect:" + redirectUrl;
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar pagamento: " + e.getMessage());
            // Volta para o formulário com os dados preenchidos
            model.addAttribute("pagamento", pagamento);
            model.addAttribute("pedidos", pedidoService.buscarTodosOsPedidos());
            model.addAttribute("formasPagamento", formaDePagamentoService.listarTodas());
            model.addAttribute("pedidoSelecionadoId", pedidoId);
            model.addAttribute("formaSelecionadaId", formaPagamentoId);
            return "pagamentos/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro inesperado ao salvar pagamento: " + e.getMessage());
            // Logar erro
            model.addAttribute("pagamento", pagamento);
            // ... (adicionar listas ao model novamente)
            return "pagamentos/form";
        }
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Integer pedidoId = null;
        try {
            Pagamento pagamento = pagamentoService.buscarPorId(id); // Busca para saber a qual pedido pertence
            pedidoId = pagamento.getPedido().getNumeroPedido();
            pagamentoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento excluído com sucesso!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Pagamento não encontrado com ID: " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir pagamento: " + e.getMessage());
            // Logar erro
        }
        // Redireciona para a lista de pagamentos do pedido, se possível, senão para a
        // lista geral
        return "redirect:" + (pedidoId != null ? "/pagamentos/pedido/" + pedidoId : "/pagamentos");
    }

    @GetMapping("/relatorio")
    public String relatorio(Model model) {
        List<Pagamento> todosOsPagamentos = pagamentoService.listarTodos();
        Double valorTotal = 0.0;
        for (Pagamento pagamento : todosOsPagamentos) {
            if (pagamento.getValorPago() != null) {
                valorTotal += pagamento.getValorPago();
            }
        }
        model.addAttribute("pagamentos", todosOsPagamentos);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("quantidadePagamentos", todosOsPagamentos.size());
        return "pagamentos/relatorio"; // Criar view
    }
}
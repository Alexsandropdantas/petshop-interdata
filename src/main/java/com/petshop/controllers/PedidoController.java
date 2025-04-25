package com.petshop.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

import com.petshop.model.ItemPedido;
import com.petshop.model.Pedido;
import com.petshop.model.Produto;
import com.petshop.services.AnimalService;
import com.petshop.services.ClienteService;
import com.petshop.services.PedidoService;
import com.petshop.services.ProdutoService;
import com.petshop.services.VendedorService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;
    private final VendedorService vendedorService;
    private final ClienteService clienteService;

    @Autowired
    public PedidoController(PedidoService pedidoService, ProdutoService produtoService,
                           VendedorService vendedorService, ClienteService clienteService,
                           AnimalService animalService) {
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
        this.vendedorService = vendedorService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarTodos(Model model) {
        List<Pedido> pedidos = pedidoService.listarTodos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos/lista";
    }

    @GetMapping("/{numeroPedido}")
    public String visualizar(@PathVariable Integer numeroPedido, Model model) {
        Pedido pedido = pedidoService.buscarPorId(numeroPedido);
        model.addAttribute("pedido", pedido);
        return "pedidos/visualizar";
    }

    @GetMapping("/novo")
    public String iniciarNovoPedido(Model model, HttpSession session) {
        // Cria um novo pedido com a data atual
        Pedido novoPedido = new Pedido();
        novoPedido.setDataEHora(LocalDateTime.now());
        novoPedido.setDesconto(0.0);
        
        // Salva o pedido para gerar o número automático
        novoPedido = pedidoService.salvar(novoPedido);
        
        // Coloca o pedido na sessão para uso durante o processo de criação
        session.setAttribute("pedidoAtual", novoPedido);
        
        // Configura o modelo para a página de formulário de itens
        model.addAttribute("pedido", novoPedido);
        model.addAttribute("itemPedido", new ItemPedido());
        model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
        model.addAttribute("vendedores", vendedorService.buscarTodosOsVendedores());
        model.addAttribute("clientes", clienteService.buscarTodosOsClientes());
        
        return "pedidos/adicionar-itens";
    }
    
    @PostMapping("/adicionar-item")
    public String adicionarItem(@ModelAttribute ItemPedido itemPedido, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // Recupera o pedido da sessão
            Pedido pedido = (Pedido) session.getAttribute("pedidoAtual");
            
            if (pedido == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Sessão de pedido expirada. Por favor, inicie um novo pedido.");
                return "redirect:/pedidos/novo";
            }
            
            // Configura o item com o pedido atual
            itemPedido.setPedido(pedido);
            
            // Carrega os objetos completos a partir dos IDs

            Produto produto = produtoService.buscarPorId(itemPedido.getProduto().getId())
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + itemPedido.getProduto().getId()));
            
            // Define o preço unitário com base no produto se não for especificado
            if (itemPedido.getPrecoUnitario() == null || itemPedido.getPrecoUnitario() == 0.0) {
                itemPedido.setPrecoUnitario(produto.getPreco());
            }
            
            // Adiciona o item ao pedido
            pedido.adicionarItem(itemPedido);
            
            // Atualiza o pedido no banco de dados
            pedidoService.atualizar(pedido.getNumeroPedido(), pedido);
            
            // Atualiza o pedido na sessão
            session.setAttribute("pedidoAtual", pedido);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Item adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao adicionar item: " + e.getMessage());
        }
        
        return "redirect:/pedidos/editar-itens";
    }
    
    @GetMapping("/editar-itens")
    public String editarItens(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Recupera o pedido da sessão
        Pedido pedido = (Pedido) session.getAttribute("pedidoAtual");
        
        if (pedido == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Sessão de pedido expirada. Por favor, inicie um novo pedido.");
            return "redirect:/pedidos/novo";
        }
        
        // Atualiza o pedido com a versão mais recente do banco de dados
        pedido = pedidoService.buscarPorId(pedido.getNumeroPedido());
        session.setAttribute("pedidoAtual", pedido);
        
        // Configura o modelo para a página
        model.addAttribute("pedido", pedido);
        model.addAttribute("itemPedido", new ItemPedido());
        model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
        model.addAttribute("vendedores", vendedorService.buscarTodosOsVendedores());
        model.addAttribute("clientes", clienteService.buscarTodosOsClientes());
        
        return "pedidos/adicionar-itens";
    }
    
    @GetMapping("/remover-item/{itemId}")
    public String removerItem(@PathVariable Long itemId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // Recupera o pedido da sessão
            Pedido pedido = (Pedido) session.getAttribute("pedidoAtual");
            
            if (pedido == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Sessão de pedido expirada. Por favor, inicie um novo pedido.");
                return "redirect:/pedidos/novo";
            }
            
            // Remove o item do pedido (pode precisar de implementação específica)
            pedidoService.removerItem(pedido.getNumeroPedido(), itemId);
            
            // Atualiza o pedido na sessão
            pedido = pedidoService.buscarPorId(pedido.getNumeroPedido());
            session.setAttribute("pedidoAtual", pedido);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Item removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao remover item: " + e.getMessage());
        }
        
        return "redirect:/pedidos/editar-itens";
    }
    
    @PostMapping("/finalizar")
    public String finalizarPedido(@RequestParam(required = false) Double desconto, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // Recupera o pedido da sessão
            Pedido pedido = (Pedido) session.getAttribute("pedidoAtual");
            
            if (pedido == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Sessão de pedido expirada. Por favor, inicie um novo pedido.");
                return "redirect:/pedidos/novo";
            }
            
            // Aplica o desconto se houver
            if (desconto != null) {
                pedido.setDesconto(desconto);
            }
            
            // Finaliza o pedido
            pedidoService.atualizar(pedido.getNumeroPedido(), pedido);
            
            // Remove o pedido da sessão
            session.removeAttribute("pedidoAtual");
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido #" + pedido.getNumeroPedido() + " finalizado com sucesso!");
            return "redirect:/pagamentos/novo?pedidoId=" + pedido.getNumeroPedido();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao finalizar pedido: " + e.getMessage());
            return "redirect:/pedidos/editar-itens";
        }
    }
    
    @GetMapping("/cancelar")
    public String cancelarPedido(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // Recupera o pedido da sessão
            Pedido pedido = (Pedido) session.getAttribute("pedidoAtual");
            
            if (pedido != null) {
                // Cancela o pedido (exclui do banco)
                pedidoService.excluir(pedido.getNumeroPedido());
                
                // Remove o pedido da sessão
                session.removeAttribute("pedidoAtual");
                
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido cancelado com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cancelar pedido: " + e.getMessage());
        }
        
        return "redirect:/pedidos";
    }
}
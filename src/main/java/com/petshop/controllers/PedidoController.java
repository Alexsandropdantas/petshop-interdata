package com.petshop.controllers;

import java.time.LocalDateTime;

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
    private final AnimalService animalService; // Adicionar AnimalService

    @Autowired
    public PedidoController(PedidoService pedidoService, ProdutoService produtoService,
            VendedorService vendedorService, ClienteService clienteService,
            AnimalService animalService) { // Injetar AnimalService
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
        this.vendedorService = vendedorService;
        this.clienteService = clienteService;
        this.animalService = animalService; // Atribuir
    }

    // ... (listarTodos, visualizar - sem mudanças) ...

    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "pedidos/lista";
    }

    @GetMapping("/novo")
    public String iniciarNovoPedido(HttpSession session, RedirectAttributes redirectAttributes) {
        // Cria um novo pedido com valores padrão
        Pedido novoPedido = new Pedido();
        novoPedido.setDataHora(LocalDateTime.now());
        novoPedido.setDesconto(0.0);

        try {
            // Salva o pedido inicial para obter o ID
            novoPedido = pedidoService.salvar(novoPedido);

            // Coloca o ID do pedido na sessão (mais leve que o objeto inteiro)
            session.setAttribute("pedidoAtualId", novoPedido.getNumeroPedido());

            // Redireciona para a tela de edição/adição de itens
            return "redirect:/pedidos/editar-itens";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao iniciar novo pedido: " + e.getMessage());
            return "redirect:/pedidos";
        }
    }

    @GetMapping("/editar-itens")
    public String editarItens(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Integer pedidoId = (Integer) session.getAttribute("pedidoAtualId");

        if (pedidoId == null) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Nenhum pedido em andamento. Por favor, inicie um novo pedido.");
            return "redirect:/pedidos"; // Ou /pedidos/novo
        }

        try {
            Pedido pedido = pedidoService.buscarPorId(pedidoId);

            model.addAttribute("pedido", pedido);
            model.addAttribute("itemPedido", new ItemPedido()); // Para o form de novo item
            model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
            model.addAttribute("vendedores", vendedorService.buscarTodosOsVendedores());
            model.addAttribute("clientes", clienteService.buscarTodosOsClientes());
            model.addAttribute("animais", animalService.buscarTodosOsAnimais()); // Lista de animais

            return "pedidos/adicionar-itens"; // Nome da view para adicionar/editar itens

        } catch (EntityNotFoundException e) {
            session.removeAttribute("pedidoAtualId"); // Limpa sessão inválida
            redirectAttributes.addFlashAttribute("mensagemErro", "Pedido não encontrado. Iniciando um novo.");
            return "redirect:/pedidos/novo";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar pedido: " + e.getMessage());
            return "redirect:/pedidos";
        }
    }

    @PostMapping("/adicionar-item")
    public String adicionarItem(@ModelAttribute ItemPedido itemPedido, // Thymeleaf deve popular os IDs: produto.id,
                                                                       // vendedor.id, cliente.id, animal.id
            @RequestParam("quantidade") Integer quantidade,
            @RequestParam(value = "precoUnitario", required = false) Double precoUnitario, // Preço pode vir do form ou
                                                                                           // ser pego do produto
            HttpSession session, RedirectAttributes redirectAttributes) {

        Integer pedidoId = (Integer) session.getAttribute("pedidoAtualId");
        if (pedidoId == null) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Sessão de pedido expirada. Por favor, inicie um novo pedido.");
            return "redirect:/pedidos/novo";
        }

        try {
            // Seta os atributos que não vêm diretamente do @ModelAttribute
            itemPedido.setQuantidade(quantidade);
            itemPedido.setPrecoUnitario(precoUnitario); // Service vai tratar se for null

            // O service agora valida e busca as entidades
            pedidoService.adicionarItemAoPedido(pedidoId, itemPedido);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Item adicionado com sucesso!");

        } catch (IllegalArgumentException | EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao adicionar item: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro inesperado ao adicionar item: " + e.getMessage());
            // Logar erro e.printStackTrace();
        }

        return "redirect:/pedidos/editar-itens"; // Volta para a tela de edição
    }

    @GetMapping("/remover-item/{itemId}")
    public String removerItem(@PathVariable Integer itemId, // ID do ItemPedido é Long
            HttpSession session, RedirectAttributes redirectAttributes) {

        Integer pedidoId = (Integer) session.getAttribute("pedidoAtualId");
        if (pedidoId == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Sessão de pedido expirada.");
            return "redirect:/pedidos/novo";
        }

        try {
            pedidoService.removerItem(pedidoId, itemId);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Item removido com sucesso!");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao remover item: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro inesperado ao remover item: " + e.getMessage());
            // Logar erro
        }

        return "redirect:/pedidos/editar-itens";
    }

    @PostMapping("/finalizar")
    public String finalizarPedido(@RequestParam(required = false) Double desconto, // Desconto pode vir do form
            HttpSession session, RedirectAttributes redirectAttributes) {

        Integer pedidoId = (Integer) session.getAttribute("pedidoAtualId");
        if (pedidoId == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Sessão de pedido expirada.");
            return "redirect:/pedidos/novo";
        }

        try {
            Pedido pedido = pedidoService.buscarPorId(pedidoId);

            // Aplica desconto se informado
            if (desconto != null && desconto >= 0) {
                pedido.setDesconto(desconto);
                pedidoService.salvar(pedido); // Salva o pedido com o desconto atualizado
            }

            // Remove o ID da sessão para indicar que o pedido foi finalizado
            session.removeAttribute("pedidoAtualId");

            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Pedido #" + pedidoId + " finalizado! Proceda para o pagamento.");
            // Redireciona para a tela de novo pagamento, passando o ID do pedido
            return "redirect:/pagamentos/novo?pedidoId=" + pedidoId;

        } catch (EntityNotFoundException e) {
            session.removeAttribute("pedidoAtualId");
            redirectAttributes.addFlashAttribute("mensagemErro", "Pedido não encontrado para finalizar.");
            return "redirect:/pedidos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao finalizar pedido: " + e.getMessage());
            return "redirect:/pedidos/editar-itens"; // Volta para edição em caso de erro
        }
    }

    @GetMapping("/cancelar")
    public String cancelarPedido(HttpSession session, RedirectAttributes redirectAttributes) {
        Integer pedidoId = (Integer) session.getAttribute("pedidoAtualId");

        if (pedidoId != null) {
            try {
                pedidoService.excluir(pedidoId); // Exclui o pedido do banco
                session.removeAttribute("pedidoAtualId"); // Limpa a sessão
                redirectAttributes.addFlashAttribute("mensagemSucesso",
                        "Pedido #" + pedidoId + " cancelado com sucesso!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensagemErro",
                        "Erro ao cancelar pedido #" + pedidoId + ": " + e.getMessage());
                // Logar erro
            }
        } else {
            redirectAttributes.addFlashAttribute("mensagemInfo", "Nenhum pedido em andamento para cancelar.");
        }

        return "redirect:/pedidos"; // Volta para a lista de pedidos
    }

}
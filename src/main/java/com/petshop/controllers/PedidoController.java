package com.petshop.controllers;

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

import com.petshop.model.ItemDePedido;
import com.petshop.model.Pedido;
import com.petshop.services.AnimalService;
import com.petshop.services.ClienteService;
import com.petshop.services.ItemDePedidoService;
import com.petshop.services.PedidoService;
import com.petshop.services.ProdutoService;
import com.petshop.services.VendedorService;

@Controller
@RequestMapping("/pedidos") // Define um prefixo para as rotas do controlador
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItemDePedidoService itemDePedidoService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private VendedorService vendedorService;


    @GetMapping
    public String listarPedidos(Model model){
        model.addAttribute("pedidos", pedidoService.buscarTodosOsPedidos());
        return "pedidos/lista";
    }


    @GetMapping("/novo")
    public String mostrarFormularioPedido(Model model) {

        Pedido novoPedido = pedidoService.criarNovoPedido();
        model.addAttribute("pedido", novoPedido);

        model.addAttribute("animais", animalService.buscarTodosOsAnimais());
        model.addAttribute("clientes", clienteService.buscarTodosOsClientes());
        model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
        model.addAttribute("vendedores", vendedorService.buscarTodosOsVendedores());

        return "pedidos/cadastro";
    }

    @GetMapping("/{id}")
    public String editarPedido(@PathVariable Integer id, Model model) {

        Pedido pedido = pedidoService.buscarPorId(id);
        model.addAttribute("pedido", pedido);

        model.addAttribute("animais", animalService.buscarTodosOsAnimais());
        model.addAttribute("clientes", clienteService.buscarTodosOsClientes());
        model.addAttribute("produtos", produtoService.buscarTodosOsProdutos());
        model.addAttribute("vendedores", vendedorService.buscarTodosOsVendedores());
        model.addAttribute("itensDoPedido", itemDePedidoService.buscarPorNumeroDoPedido(id));

        return "pedidos/editar";
    }

    // Salvar o novo item de pedido e redirecionar para carregar novamente a tela de pedido
    @PostMapping()
    public String adicionarItemAoPedido(
            @ModelAttribute ItemDePedido itemDePedido,
            @RequestParam("numeroPedido") Integer numeroPedido,
            // Parâmetros vindos do formulário
            @RequestParam("produtoId") Integer produtoId,
            @RequestParam("desconto") Double desconto,
            @RequestParam("quantidade") Integer quantidade,
            @RequestParam("clienteId") Integer clienteId,
            @RequestParam("animalId") Integer animalId,
            @RequestParam("vendedorId") Integer vendedorId,
            RedirectAttributes redirectAttributes) {

        try {
            itemDePedidoService.adicionarItemDePedido(numeroPedido, produtoId, quantidade, clienteId, animalId, vendedorId, desconto);
        } catch (Exception e) {
            // Logar erro e retornar mensagem
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao adicionar item: " + e.getMessage());
        }

        // Recarrega a mesma página do pedido
        return "redirect:/pedidos/" + numeroPedido;
    }

}
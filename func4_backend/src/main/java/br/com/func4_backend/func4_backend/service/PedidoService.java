package br.com.func4_backend.func4_backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.func4_backend.func4_backend.exception.ResourceNotFoundException;
import br.com.func4_backend.func4_backend.model.OrderItem;
import br.com.func4_backend.func4_backend.model.Pedido;
import br.com.func4_backend.func4_backend.model.Produto;
import br.com.func4_backend.func4_backend.repo.OrderItemRepo;
import br.com.func4_backend.func4_backend.repo.PedidoRepo;
import br.com.func4_backend.func4_backend.repo.ProdutoRepo;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepo pedidoRepo;
    private final OrderItemRepo itemRepo;
    private final ProdutoRepo produtoRepo;
    private final HistoricoPedidoService orderHistoryService;

    public PedidoService(PedidoRepo pedidoRepo, OrderItemRepo itemRepo,
                         ProdutoRepo produtoRepo, HistoricoPedidoService orderHistoryService) {
        this.pedidoRepo = pedidoRepo;
        this.itemRepo = itemRepo;
        this.produtoRepo = produtoRepo;
        this.orderHistoryService = orderHistoryService;
    }

    public List<Pedido> listar() {
        return pedidoRepo.findAll();
    }

    public Pedido buscar(Long id) {
        return pedidoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new RuntimeException("Pedido deve ter ao menos 1 item");
        }

        BigDecimal total = BigDecimal.ZERO;

        // Primeiro: carregar os produtos corretos do BD
        for (OrderItem item : pedido.getItens()) {

            Long idProduto = item.getProduto().getId();

            Produto produtoBD = produtoRepo.findById(idProduto)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + idProduto));

            // Verificar estoque
            if (produtoBD.getEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produtoBD.getNome());
            }

            // Preço unitário vem do BD (segurança)
            item.setPrecoUnitario(produtoBD.getPreco());

            // Prepara o vínculo inverso
            item.setPedido(pedido);

            // Soma total
            BigDecimal parcial = produtoBD.getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));

            total = total.add(parcial);
        }

        // Atualiza total do pedido
        pedido.setTotal(total);

        // Salvar pedido primeiro (ID gerado)
        Pedido pedidoSalvo = pedidoRepo.save(pedido);

        // Agora salvar itens com pedido_id
        for (OrderItem item : pedido.getItens()) {
            item.setPedido(pedidoSalvo);
            itemRepo.save(item);
        }

        return pedidoSalvo;
    }


    @Transactional
    public Pedido cancelar(Long id) {
        Pedido p = buscar(id);
        if ("CANCELADO".equalsIgnoreCase(p.getStatus())) {
            throw new IllegalArgumentException("Pedido já está cancelado");
        }
        // apenas permitir cancelamento em certos status (ex: PENDENTE)
        if (!"PENDENTE".equalsIgnoreCase(p.getStatus()) && !"ABERTO".equalsIgnoreCase(p.getStatus())) {
            throw new IllegalArgumentException("Não é possível cancelar pedido no status: " + p.getStatus());
        }

        // devolver estoque
        for (OrderItem item : p.getItens()) {
            Produto prod = produtoRepo.findById(item.getProduto().getId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
            prod.setEstoque(prod.getEstoque() + item.getQuantidade());
            produtoRepo.save(prod);
        }

        p.setStatus("CANCELADO");
        Pedido updated = pedidoRepo.save(p);
        orderHistoryService.logEvent(p.getId(), "CANCELLED", "Pedido cancelado");
        return updated;
    }
}

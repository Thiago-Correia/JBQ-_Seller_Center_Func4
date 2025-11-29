package br.com.func4_backend.func4_backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.func4_backend.func4_backend.exception.ParametersNotMetException;
import br.com.func4_backend.func4_backend.exception.ResourceNotFoundException;
import br.com.func4_backend.func4_backend.model.OrderItem;
import br.com.func4_backend.func4_backend.model.Pedido;
import br.com.func4_backend.func4_backend.model.Produto;
import br.com.func4_backend.func4_backend.repo.OrderItemRepo;
import br.com.func4_backend.func4_backend.repo.PedidoRepo;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepo pedidoRepo;
    @Autowired
    private OrderItemRepo itemRepo;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private HistoricoPedidoService historicoPedidoService;


    
    @Transactional
    public Pedido create(Pedido pedido) {

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new ParametersNotMetException("Pedido deve ter ao menos 1 item");
        }

        checarEstoque(pedido);

        BigDecimal total = calcularTotal(pedido);
        pedido.setTotal(total);

        atualizarEstoque(pedido, true);

        Pedido salvo = pedidoRepo.save(pedido);

        historicoPedidoService.create(pedido);

        for (OrderItem item : pedido.getItens()) {
            item.setPedido(salvo);
            itemRepo.save(item);
        }

        return salvo;
    }

    @Transactional
    public Pedido updateWithStock(Long id, Pedido pedidoNovo) {

        Pedido pedidoVelho = buscar(id);

        if (pedidoNovo.getItens() == null || pedidoNovo.getItens().isEmpty()) {
            throw new ParametersNotMetException("Pedido deve ter ao menos 1 item");
        }

        checarEstoqueAtualizacao(pedidoNovo, pedidoVelho);

        BigDecimal total = calcularTotal(pedidoNovo);
        pedidoNovo.setTotal(total);

        reconciliarEstoque(pedidoNovo, pedidoVelho);

        pedidoNovo.setId(id);
        Pedido salvo = pedidoRepo.save(pedidoNovo);

        historicoPedidoService.update(pedidoNovo, "Estoque atualizado");

        itemRepo.deleteByPedidoId(id);
        for (OrderItem item : pedidoNovo.getItens()) {
            item.setPedido(salvo);
            itemRepo.save(item);
        }

        return salvo;
    }


    @Transactional
    public Pedido cancelar(Long id) {
        Pedido pedido = buscar(id);

        if ("CANCELADO".equalsIgnoreCase(pedido.getStatus())) {
            throw new ParametersNotMetException("Pedido já está cancelado");
        }

        // Devolve estoque produto por produto
        for (OrderItem item : pedido.getItens()) {
            Produto produto = produtoService.buscar(item.getProduto().getId());
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoService.save(produto);
        }

        pedido.setStatus("CANCELADO");
        historicoPedidoService.cancel(pedido);
        
        return pedidoRepo.save(pedido);
    }

    @Transactional
    public void checarEstoque(Pedido pedido) {
        for (OrderItem item : pedido.getItens()) {

            Produto produto = produtoService.buscar(item.getProduto().getId());

            if (produto.getEstoque() < item.getQuantidade()) {
                throw new ParametersNotMetException(
                    "Estoque insuficiente para o produto: " + produto.getNome()
                );
            }
        }
    }

    @Transactional
    public void atualizarEstoque(Pedido pedido, boolean remover) {

        for (OrderItem item : pedido.getItens()) {

            Produto produto = produtoService.buscar(item.getProduto().getId());
            int qtd = item.getQuantidade();

            if (remover) {
                if (produto.getEstoque() < qtd) {
                    throw new ParametersNotMetException("Estoque insuficiente para o produto: " + produto.getNome());
                }
                produto.setEstoque(produto.getEstoque() - qtd);
            } else {
                produto.setEstoque(produto.getEstoque() + qtd);
            }

            produtoService.save(produto);
        }
    }



    @Transactional
    public void checarEstoqueAtualizacao(Pedido novo, Pedido velho) {

        for (OrderItem itemNovo : novo.getItens()) {

            int qtdNova = itemNovo.getQuantidade();
            Long idProduto = itemNovo.getProduto().getId();

            int qtdVelha = 0;
            for (OrderItem itemVelho : velho.getItens()) {
                if (itemVelho.getProduto().getId().equals(idProduto)) {
                    qtdVelha = itemVelho.getQuantidade();
                    break;
                }
            }

            int diferenca = qtdNova - qtdVelha;

            if (diferenca > 0) {
                Produto produto = produtoService.buscar(idProduto);
                if (produto.getEstoque() < diferenca) {
                    throw new ParametersNotMetException(
                        "Estoque insuficiente para o produto: " + produto.getNome()
                    );
                }
            }
        }
    }

    @Transactional
    public void reconciliarEstoque(Pedido novo, Pedido velho) {

        for (OrderItem itemNovo : novo.getItens()) {

            Long idProduto = itemNovo.getProduto().getId();
            int qtdNova = itemNovo.getQuantidade();

            int qtdVelha = 0;
            for (OrderItem itemVelho : velho.getItens()) {
                if (itemVelho.getProduto().getId().equals(idProduto)) {
                    qtdVelha = itemVelho.getQuantidade();
                    break;
                }
            }

            int diferenca = qtdNova - qtdVelha;

            Produto produto = produtoService.buscar(idProduto);

            if (diferenca > 0) {
                produto.setEstoque(produto.getEstoque() - diferenca);
            } else if (diferenca < 0) {
                produto.setEstoque(produto.getEstoque() + Math.abs(diferenca));
            }

            produtoService.save(produto);
        }

        for (OrderItem itemVelho : velho.getItens()) {

            Long idProdutoVelho = itemVelho.getProduto().getId();

            boolean existe = false;
            for (OrderItem itemNovo : novo.getItens()) {
                if (itemNovo.getProduto().getId().equals(idProdutoVelho)) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                Produto produto = produtoService.buscar(idProdutoVelho);
                produto.setEstoque(produto.getEstoque() + itemVelho.getQuantidade());
                produtoService.save(produto);
            }
        }
    }

    @Transactional
    public BigDecimal calcularTotal(Pedido pedido) {

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : pedido.getItens()) {

            Produto produto = produtoService.buscar(item.getProduto().getId());
            
            item.setPrecoUnitario(produto.getPreco());

            BigDecimal parcial = 
                produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));

            total = total.add(parcial);
        }

        return total;
    }

    public Pedido buscar(Long id) {
        return pedidoRepo.findById(id).orElseThrow(() -> new ParametersNotMetException("Pedido não encontrado: " + id));
    }

    public List<Pedido> listar() {
        List<Pedido> pedidos = pedidoRepo.findAll();
        if(pedidos.isEmpty()){
            throw new ResourceNotFoundException("Não há pedidos");
        }
        return pedidos;
    }
}
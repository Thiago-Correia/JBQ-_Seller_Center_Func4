package br.com.func4_backend.func4_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.func4_backend.func4_backend.model.HistoricoPedido;
import br.com.func4_backend.func4_backend.model.Pedido;
import br.com.func4_backend.func4_backend.repo.HistoricoPedidoRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class HistoricoPedidoService {

    @Autowired
    private HistoricoPedidoRepo repo;

    @PersistenceContext
    private EntityManager em;

    public List<HistoricoPedido> listByPedidoId(Long id){
        return repo.findByPedidoId(id);
    }

    public HistoricoPedido create(Pedido pedido) {
        return repo.save(new HistoricoPedido(pedido, "CREATED", "Pedido Criado"));
    }

    public HistoricoPedido update(Pedido pedido, String event_data) {
        return repo.save(new HistoricoPedido(pedido, "UPDATED", event_data));
    }

    public HistoricoPedido cancel(Pedido pedido){
        return repo.save(new HistoricoPedido(pedido, "CANCELLED", "Pedido Cancelado"));
    }
}

package br.com.func4_backend.func4_backend.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class HistoricoPedidoService {

    @PersistenceContext
    private EntityManager em;

    public void logEvent(Long pedidoId, String type, String data) {
        em.createNativeQuery(
                "INSERT INTO order_history (pedido_id, event_type, event_data) VALUES (:pedidoId, :type, :data)")
        .setParameter("pedidoId", pedidoId)
        .setParameter("type", type)
        .setParameter("data", data)
        .executeUpdate();
    }
}

package br.com.func4_backend.func4_backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.func4_backend.func4_backend.model.HistoricoPedido;

@Repository
public interface HistoricoPedidoRepo extends JpaRepository<HistoricoPedido, Number> {
    List<HistoricoPedido> findByPedidoId(Long pedidoId);
}

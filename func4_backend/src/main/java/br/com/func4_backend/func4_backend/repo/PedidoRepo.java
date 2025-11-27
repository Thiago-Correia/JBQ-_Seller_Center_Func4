package br.com.func4_backend.func4_backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.func4_backend.func4_backend.model.Pedido;

@Repository
public interface PedidoRepo extends JpaRepository<Pedido, Long>{

}

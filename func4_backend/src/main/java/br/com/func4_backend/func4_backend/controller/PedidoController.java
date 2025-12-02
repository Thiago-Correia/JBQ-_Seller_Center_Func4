package br.com.func4_backend.func4_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.func4_backend.func4_backend.model.HistoricoPedido;
import br.com.func4_backend.func4_backend.model.Pedido;
import br.com.func4_backend.func4_backend.service.PedidoService;

@RestController
@RequestMapping("/api/orders")
public class PedidoController {

    @Autowired
    private PedidoService service;


    @GetMapping
    public ResponseEntity<List<Pedido>> listar() { 
        List<Pedido> pedidos = service.listar();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) { 
        Pedido pedido = service.buscar(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoPedido>> buscarHistorico(@PathVariable Long id) { 
        List<HistoricoPedido> historico = service.listarHistorico(id);
        return ResponseEntity.ok(historico);
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        Pedido p = service.create(pedido);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Pedido> update(@RequestBody Pedido pedido, @PathVariable Long id) {
        Pedido p = service.updateEstoque(id,pedido);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long id) {
        Pedido p = service.cancelar(id);
        return ResponseEntity.ok(p);
    }
}

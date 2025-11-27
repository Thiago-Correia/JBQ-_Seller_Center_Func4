package br.com.func4_backend.func4_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.func4_backend.func4_backend.model.Pedido;
import br.com.func4_backend.func4_backend.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pedido> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        Pedido p = service.salvar(pedido);
        return ResponseEntity.status(201).body(p);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long id) {
        Pedido p = service.cancelar(id);
        return ResponseEntity.ok(p);
    }
}

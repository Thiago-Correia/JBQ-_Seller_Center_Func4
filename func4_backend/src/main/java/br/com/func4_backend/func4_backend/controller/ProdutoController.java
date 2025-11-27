package br.com.func4_backend.func4_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.func4_backend.func4_backend.model.Produto;
import br.com.func4_backend.func4_backend.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) { this.service = service; }

    @GetMapping
    public List<Produto> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto p = service.criar(produto);
        return ResponseEntity.status(201).body(p);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return service.atualizar(id, produto);
    }

    @PatchMapping("/excluir/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok().build();
    }
}

package br.com.func4_backend.func4_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.func4_backend.func4_backend.exception.ResourceNotFoundException;
import br.com.func4_backend.func4_backend.model.Produto;
import br.com.func4_backend.func4_backend.repo.ProdutoRepo;
import jakarta.transaction.Transactional;


@Service
public class ProdutoService {

    private final ProdutoRepo produtoRepo;
    private final HistoricoProdutoService historyService; // salva histórico

    public ProdutoService(ProdutoRepo produtoRepo, HistoricoProdutoService historyService) {
        this.produtoRepo = produtoRepo;
        this.historyService = historyService;
    }

    public List<Produto> listar() {
        return produtoRepo.findAll();
    }

    public Produto buscar(Long id) {
        return produtoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    @Transactional
    public Produto criar(Produto p) {
        Produto saved = produtoRepo.save(p);
        historyService.logCreate(saved);
        return saved;
    }

    @Transactional
    public Produto atualizar(Long id, Produto dados) {
        Produto atual = buscar(id);
        // gravar estado antigo no histórico
        historyService.logUpdate(atual);

        atual.setNome(dados.getNome());
        atual.setDescricao(dados.getDescricao());
        atual.setPreco(dados.getPreco());
        atual.setEstoque(dados.getEstoque());
        atual.setAtivo(dados.getAtivo());

        return produtoRepo.save(atual);
    }

    @Transactional
    public void desativar(Long id) {
        Produto atual = buscar(id);
        historyService.logUpdate(atual);
        atual.setAtivo(false);
        produtoRepo.save(atual);
    }
}

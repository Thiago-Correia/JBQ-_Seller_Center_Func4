package br.com.func4_backend.func4_backend.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.func4_backend.func4_backend.exception.ParametersNotMetException;
import br.com.func4_backend.func4_backend.exception.ResourceNotFoundException;
import br.com.func4_backend.func4_backend.model.Produto;
import br.com.func4_backend.func4_backend.repo.ProdutoRepo;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepo produtoRepo;

    public Produto save(Produto produto){
        return produtoRepo.save(produto);
    }

    public List<Produto> saveAll(Collection<Produto> produtos){
        return produtoRepo.saveAll(produtos);
    }

    public Produto buscar(Long id){
        return produtoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    public List<Produto> listar() {
        List<Produto> produtos = produtoRepo.findAll();
        if(produtos.isEmpty()){
            throw new ResourceNotFoundException("Não há produtos");
        }
        return produtos;
    }

    public List<Produto> listarPorId(Collection<Long> ids) {
        List<Produto> produtos = produtoRepo.findAllById(ids);
        if(produtos.isEmpty()){
            throw new ResourceNotFoundException("Não há produtos com esses Ids");
        }
        return produtos;
    }

    @Transactional
    public Produto atualizarEstoque(Produto produto, int quantidade){
        Produto produtoNovo = buscar(produto.getId());
        checarEstoque(produtoNovo, quantidade);
        produtoNovo.setEstoque(produtoNovo.getEstoque() - quantidade);
        return produtoRepo.save(produtoNovo);
    }

    public void checarEstoque(Produto produto, int quantidade){
        if (produto.getEstoque() < quantidade) {
            throw new ParametersNotMetException("Estoque de " + produto.getNome() + " insuficiente");
        }
    }
}

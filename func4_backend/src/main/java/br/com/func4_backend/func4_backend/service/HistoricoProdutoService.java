package br.com.func4_backend.func4_backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.func4_backend.func4_backend.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class HistoricoProdutoService {

    @PersistenceContext
    private EntityManager em;

    public void logCreate(Produto produto) {
        log(produto, "CREATE");
    }

    public void logUpdate(Produto produto) {
        log(produto, "UPDATE");
    }

    public void logSoftDelete(Produto produto) {
        log(produto, "SOFT_DELETE");
    }

    private void log(Produto p, String type) {
        Map<String,Object> m = new HashMap<>();
        m.put("produto_id", p.getId());
        m.put("old_nome", p.getNome());
        m.put("old_descricao", p.getDescricao());
        m.put("old_preco", p.getPreco());
        m.put("old_estoque", p.getEstoque());
        m.put("old_ativo", p.getAtivo());
        m.put("change_type", type);
        m.put("changed_by", "system"); // substituir por user real se houver

        // Insert direto com native SQL
        em.createNativeQuery(
                "INSERT INTO product_history (produto_id, old_nome, old_descricao, old_preco, old_estoque, old_ativo, change_type, changed_by) " +
                "VALUES (:produto_id, :old_nome, :old_descricao, :old_preco, :old_estoque, :old_ativo, :change_type, :changed_by)"
        )
        .setParameter("produto_id", m.get("produto_id"))
        .setParameter("old_nome", m.get("old_nome"))
        .setParameter("old_descricao", m.get("old_descricao"))
        .setParameter("old_preco", m.get("old_preco"))
        .setParameter("old_estoque", m.get("old_estoque"))
        .setParameter("old_ativo", m.get("old_ativo"))
        .setParameter("change_type", m.get("change_type"))
        .setParameter("changed_by", m.get("changed_by"))
        .executeUpdate();
    }
}

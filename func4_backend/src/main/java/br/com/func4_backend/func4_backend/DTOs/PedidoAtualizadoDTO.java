package br.com.func4_backend.func4_backend.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoAtualizadoDTO {
    private Long id;
    private LocalDateTime dataPedido;
    private BigDecimal total;
    private String status;
    private String clienteNome;
    private List<Long> itensID;
}

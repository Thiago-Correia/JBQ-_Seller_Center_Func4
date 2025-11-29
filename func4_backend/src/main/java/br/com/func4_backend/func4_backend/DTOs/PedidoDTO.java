package br.com.func4_backend.func4_backend.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {

    private Long id;
    private String clienteNome;
    private BigDecimal total;
    private String status;
    private LocalDateTime dataPedido;

}

package br.com.func4_backend.func4_backend.model;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    private String event; //CREATED. UPDATED, CANCELLED
    @Builder.Default
    private String event_data = "";  //SOMENTE SE EVENT FOR UPDATED
    private LocalDateTime event_time;
    
    public HistoricoPedido(Pedido pedido, String event){
        this.pedido = pedido;
        this.event = event;
    }

    public HistoricoPedido(Pedido pedido, String event, String event_data){
        this.pedido = pedido;
        this.event = event;
        this.event_data = event_data;
    }


    @PrePersist
    public void prePersist() {
        event_time = LocalDateTime.now();
    }
}


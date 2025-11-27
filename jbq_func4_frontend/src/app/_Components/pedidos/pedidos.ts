import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

interface Pedido {
  id: number;
  cliente: string;
  total: number;
  status: string;
  data: string;
}

@Component({
  selector: 'app-pedidos',
  imports: [CommonModule],
  templateUrl: './pedidos.html',
  styleUrl: './pedidos.css',
})
export class Pedidos {
  selectedPedido: Pedido | null = null;

  pedidos: Pedido[] = [
    { id: 1, cliente: 'João Silva', total: 120.5, status: 'Em andamento', data: '2025-11-25' },
    { id: 2, cliente: 'Maria Souza', total: 89.9, status: 'Concluído', data: '2025-11-26' },
    { id: 3, cliente: 'Carlos Lima', total: 45.0, status: 'Cancelado', data: '2025-11-26' },
  ];

  abrirDetalhes(pedido: Pedido) {
    this.selectedPedido = pedido;
  }

  fecharDetalhes() {
    this.selectedPedido = null;
  }
  editarPedido(id: number) {
    console.log('Editar pedido:', id);
  }

  cancelarPedido(id: number) {
    console.log('Cancelar pedido:', id);
  }
}

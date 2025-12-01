import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pedido-detalhes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pedido-detalhes.html',
  styleUrls: ['./pedido-detalhes.css'],
})
export class PedidoDetalhes {
  @Input() id: number = 0;
  @Input() cliente: string = '';
  @Input() status: string = '';
  @Input() total: number = 0;
  @Input() data: string = '';

  editarPedido(id: number) {
    console.log('Editar pedido:', id);
  }

  cancelarPedido(id: number) {
    console.log('Cancelar pedido:', id);
  }
}

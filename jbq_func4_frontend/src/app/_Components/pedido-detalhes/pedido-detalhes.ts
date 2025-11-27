import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-pedido-detalhes',
  imports: [],
  templateUrl: './pedido-detalhes.html',
  styleUrl: './pedido-detalhes.css',
})
export class PedidoDetalhes {
  @Input() id: Number = 0;
  @Input() cliente: String = '';
  @Input() status: String = '';
  @Input() total: Number = 0;
  @Input() data: String = '';

  editarPedido(id: number) {
    console.log('Editar pedido:', id);
  }

  cancelarPedido(id: number) {
    console.log('Cancelar pedido:', id);
  }
}

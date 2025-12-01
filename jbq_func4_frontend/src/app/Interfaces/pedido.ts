import { ItemPedido } from './item_pedido';

export interface Pedido {
  id: number;
  dataPedido: string;
  total: number;
  status: string;
  clienteNome: string;
  itens: ItemPedido[];
}

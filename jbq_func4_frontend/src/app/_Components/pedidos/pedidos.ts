import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PedidosService } from '../../../Services/pedidos-service';
import { Historico } from '../../Interfaces/historico';
import { Pedido } from '../../Interfaces/pedido';

@Component({
  selector: 'app-pedidos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pedidos.html',
  styleUrls: ['./pedidos.css'],
})
export class Pedidos {
  private orderService = inject(PedidosService);

  pedidos: Pedido[] = [];
  selectedPedido: Pedido | null = null;
  historico: Historico[] = [];
  abaAtiva: 'detalhes' | 'historico' = 'detalhes';
  isEditando: boolean = false;
  itensEditaveis: any[] = [];

  ngOnInit(): void {
    this.carregarPedidos();
  }

  carregarPedidos() {
    this.orderService.listar().subscribe({
      next: (data) => {
        this.pedidos = data;
      },
      error: (err) => console.error('Erro ao carregar pedidos:', err),
    });
  }

  abrirDetalhes(pedido: Pedido) {
    this.orderService.buscar(pedido.id).subscribe({
      next: (pedidoCompleto) => {
        this.selectedPedido = pedidoCompleto;
        this.abaAtiva = 'detalhes';
        console.log(this.selectedPedido);
      },
      error: (err) => console.error('Erro ao buscar detalhes:', err),
    });
  }

  fecharDetalhes() {
    this.selectedPedido = null;
    this.historico = [];
    this.isEditando = false;
  }

  habilitarEdicao() {
    if (!this.selectedPedido) {
      console.warn('habilitarEdicao: nenhum pedido selecionado');
      return;
    }

    this.isEditando = true;

    this.itensEditaveis = this.selectedPedido.itens.map((i) => ({
      produto: { id: i.produto.id },
      quantidade: i.quantidade,
      precoUnitario: i.precoUnitario,
    }));
  }

  confirmarEdicao() {
    if (!this.selectedPedido) return;

    const payload = {
      clienteNome: this.selectedPedido.clienteNome,
      status: this.selectedPedido.status,
      itens: this.itensEditaveis.map((i) => ({
        produto: { id: i.produto.id },
        quantidade: i.quantidade,
      })),
      dataPedido: this.selectedPedido.dataPedido,
    };

    console.log('JSON enviado para editarPedido():', payload);

    this.editarPedido(payload);
    this.isEditando = false;
  }

  carregarHistorico() {
    if (!this.selectedPedido) return;

    this.orderService.listarHistorico(this.selectedPedido.id).subscribe({
      next: (hist) => {
        this.historico = hist;
        this.abaAtiva = 'historico';
      },
      error: (err) => console.error('Erro ao carregar histórico:', err),
    });
  }

  editarPedido(payload: any) {
    if (!this.selectedPedido) return;

    this.orderService.update(this.selectedPedido.id, payload).subscribe({
      next: () => {
        alert('Pedido atualizado!');

        // Atualiza localmente as quantidades na tela
        this.selectedPedido!.itens.forEach((item, i) => {
          item.quantidade = this.itensEditaveis[i].quantidade;
        });
      },

      error: (err) => {
        console.error(err);

        if (err.status === 409) {
          alert('⚠ Estoque insuficiente para um dos produtos.');
        } else {
          alert('Erro ao atualizar pedido.');
        }
      },
    });
  }

  cancelarPedido(id: number) {
    this.orderService.cancelar(id).subscribe({
      next: () => {
        this.fecharDetalhes();
        this.carregarPedidos();
      },
      error: (err) => console.error('Erro ao cancelar pedido:', err),
    });
  }

  formatarData(data: string | null | undefined): string {
    if (!data) return '';

    const d = new Date(data);

    if (isNaN(d.getTime())) return data;

    const dia = d.getDate().toString().padStart(2, '0');
    const mes = (d.getMonth() + 1).toString().padStart(2, '0');
    const ano = d.getFullYear();

    const hora = d.getHours().toString().padStart(2, '0');
    const min = d.getMinutes().toString().padStart(2, '0');

    return `${dia}/${mes}/${ano} ${hora}:${min}`;
  }
}

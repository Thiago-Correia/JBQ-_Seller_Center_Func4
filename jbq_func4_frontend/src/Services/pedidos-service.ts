import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Historico } from '../app/Interfaces/historico';
import { Pedido } from '../app/Interfaces/pedido';

@Injectable({
  providedIn: 'root',
})
export class PedidosService {
  private apiUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) {}

  /** GET /api/orders */
  listar(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(this.apiUrl);
  }

  /** GET /api/orders/{id} */
  buscar(id: number): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.apiUrl}/${id}`);
  }

  listarHistorico(id: number): Observable<Historico[]> {
    return this.http.get<Historico[]>(`${this.apiUrl}/${id}/historico`);
  }

  /** POST /api/orders/{id}/update */
  update(id: number, pedido: Pedido): Observable<Pedido> {
    return this.http.post<Pedido>(`${this.apiUrl}/${id}/update`, pedido);
  }

  /** POST /api/orders/{id}/cancel */
  cancelar(id: number): Observable<Pedido> {
    return this.http.post<Pedido>(`${this.apiUrl}/${id}/cancel`, {});
  }
}

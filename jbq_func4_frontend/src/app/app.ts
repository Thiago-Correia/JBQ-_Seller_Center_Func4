import { Component, signal } from '@angular/core';
import { Pedidos } from './_Components/pedidos/pedidos';

@Component({
  selector: 'app-root',
  imports: [Pedidos],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('jbq_func4_frontend');
}

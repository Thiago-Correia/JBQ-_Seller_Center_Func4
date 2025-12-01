import { Component, signal } from '@angular/core';
import { Pedidos } from './_Components/pedidos/pedidos';
import { HeaderComponent } from './_Components/header/header.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, Pedidos],
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class App {
  protected readonly title = signal('jbq_func4_frontend');
}

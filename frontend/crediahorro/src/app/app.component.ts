import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { BusquedaService } from './services/busqueda.service'; // Ajusta la ruta según tu estructura

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule],
  template: `
    <div class="d-flex flex-column min-vh-100">
      <!-- Navbar -->
      <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
          <a class="navbar-brand" routerLink="/clientes">
            <i class="bi bi-cash-stack me-2"></i>CrediAhorro
          </a>
          <button
            class="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                  <li class="nav-item">
                    <a class="nav-link" routerLink="/clientes" routerLinkActive="active">
                      Clientes
                    </a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" routerLink="/reportes" routerLinkActive="active">
                      Reportes
                    </a>
                  </li>
                  <li class="nav-item">
                                      <a class="nav-link" routerLink="/graficos" routerLinkActive="active">
                                        Gráficos
                                      </a>
                                    </li>
                </ul>
            <form class="d-flex ms-auto" (submit)="onBuscarCliente($event)">
              <input
                class="form-control me-2"
                id="buscadorInput"
                type="search"
                placeholder="Buscar Nombre"
                aria-label="Search"
              />
              <button class="btn btn-outline-light" type="submit">
                <i class="bi bi-search"></i>
              </button>
            </form>
          </div>
        </div>
      </nav>

      <!-- Content -->
      <main class="flex-fill container my-4">
        <router-outlet></router-outlet>
      </main>

      <!-- Footer -->
      <footer class="bg-dark text-light py-3">
        <div class="container text-center">
          <p class="mb-1">&copy; 2025 CrediAdmin. Todos los derechos reservados.</p>
          <div class="d-flex justify-content-center">
            <a href="#" class="text-light mx-2"><i class="bi bi-facebook"></i></a>
            <a href="#" class="text-light mx-2"><i class="bi bi-instagram"></i></a>
            <a href="#" class="text-light mx-2"><i class="bi bi-linkedin"></i></a>
            <a href="#" class="text-light mx-2"><i class="bi bi-envelope-fill"></i></a>
          </div>
        </div>
      </footer>
    </div>
  `,
  styles: []
})
export class AppComponent {
  constructor(private busquedaService: BusquedaService) {}

  onBuscarCliente(event: Event) {
    event.preventDefault();
    const input = (event.target as HTMLFormElement).querySelector<HTMLInputElement>('#buscadorInput');
    const value = input?.value.trim().toLowerCase();
    if (value) {
      this.busquedaService.buscar(value);
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ClienteService, Cliente } from '../../services/cliente.service';
import { BusquedaService } from '../../services/busqueda.service';
import { Subscription } from 'rxjs';
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cliente-list.component.html',
  styleUrls: ['./cliente-list.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ClienteListComponent implements OnInit, OnDestroy {
  clientes: Cliente[] = [];
  busquedaSubscription!: Subscription;

  constructor(
    private clienteService: ClienteService,
    private busquedaService: BusquedaService
  ) {}

  ngOnInit(): void {
    this.cargarClientes();

    this.busquedaSubscription = this.busquedaService.terminoBusqueda$.subscribe(
      termino => this.buscarCliente(termino)
    );
  }

  ngOnDestroy(): void {
    this.busquedaSubscription?.unsubscribe();
  }

  cargarClientes(): void {
    this.clienteService.getClientes().subscribe(
      data => this.clientes = data,
      error => console.error(error)
    );
  }

  eliminarCliente(id: number): void {
    this.clienteService.eliminarCliente(id).subscribe(
      () => this.cargarClientes(),
      error => console.error(error)
    );
  }

  buscarCliente(searchValue: string): void {
    const rows = document.querySelectorAll("tbody tr");
    let found = false;

    // Eliminar resaltados anteriores
    const resaltados = document.querySelectorAll(".resaltado");
    resaltados.forEach(span => {
      const parent = span.parentNode;
      if (parent) {
        parent.replaceChild(document.createTextNode(span.textContent || ''), span);
        parent.normalize();
      }
    });

    for (const row of rows) {
      const nombreCell = (row as HTMLTableRowElement).cells[0];
      const dniCell = (row as HTMLTableRowElement).cells[1];

      const nombreText = nombreCell.textContent?.toLowerCase() || '';
      const dniText = dniCell.textContent?.toLowerCase() || '';

      if (nombreText.includes(searchValue.toLowerCase()) || dniText.includes(searchValue.toLowerCase())) {
        found = true;
        this.highlightText(nombreCell, searchValue);
        this.highlightText(dniCell, searchValue);
        row.scrollIntoView({ behavior: "smooth", block: "center" });
        break; // Solo al primer resultado
      }
    }

    if (!found) {
      alert("No se encontró ningún cliente con ese nombre o DNI.");
    }
  }

  highlightText(cell: HTMLTableCellElement, searchValue: string) {
    const text = cell.textContent || '';
    const regex = new RegExp(`(${searchValue})`, 'ig');
    const highlightedText = text.replace(regex, `<span class="resaltado">$1</span>`);
    cell.innerHTML = highlightedText;
  }
}

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
  clientesAgrupadosPorMes: { [mesAnio: string]: Cliente[] } = {}

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
      data => {
        const clientes = data.reverse();
        const hoyDate = new Date();

        clientes.forEach(cliente => {
          cliente.cuotaPendienteTexto = '';

          if (cliente.prestamos && cliente.prestamos.length > 0) {
            const prestamosOrdenados = [...cliente.prestamos].sort((a, b) => {
              return new Date(b.fechaCreacion!).getTime() - new Date(a.fechaCreacion!).getTime();
            });

            const prestamoMasReciente = prestamosOrdenados[0];
            cliente.estadoPrestamoMasReciente = prestamoMasReciente.estado;

            if (prestamoMasReciente.cuotas) {
              // Buscamos la próxima cuota pendiente en los próximos 7 días
              const cuotaCercana = prestamoMasReciente.cuotas.find(cuota => {
                if (cuota.estado !== 'PENDIENTE') return false;
                const fechaPagoDate = new Date(cuota.fechaPago);
                const diffTime = fechaPagoDate.getTime() - hoyDate.getTime();
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                return diffDays >= 0 && diffDays <= 7;
              });

              if (cuotaCercana) {
                const fechaPagoDate = new Date(cuotaCercana.fechaPago);
                const diffDays = Math.ceil((fechaPagoDate.getTime() - hoyDate.getTime()) / (1000 * 60 * 60 * 24));
                cliente.cuotaPendienteTexto =
                  diffDays === 0 ? 'Hoy vence una cuota' :
                  `En ${diffDays} día${diffDays === 1 ? '' : 's'} vence una cuota`;
              } else {
                // Si NO hay cuota en los próximos 7 días, revisamos si hay alguna VENCIDA
                const cuotaVencida = prestamoMasReciente.cuotas.find(cuota => {
                  if (cuota.estado !== 'PENDIENTE') return false;
                  const fechaPagoDate = new Date(cuota.fechaPago);
                  return fechaPagoDate.getTime() < hoyDate.getTime();
                });

                if (cuotaVencida) {
                  const fechaPagoDate = new Date(cuotaVencida.fechaPago);
                  const diffDays = Math.floor((hoyDate.getTime() - fechaPagoDate.getTime()) / (1000 * 60 * 60 * 24));
                  cliente.cuotaPendienteTexto = `Venció una cuota hace ${diffDays} día${diffDays === 1 ? '' : 's'}`;
                }
              }
            }

          } else {
            cliente.estadoPrestamoMasReciente = 'SIN_PRESTAMO';
          }
        });

        this.clientes = clientes;
        this.clientesAgrupadosPorMes = this.agruparClientesPorMes(clientes);
      },
      error => console.error(error)
    );
  }

  agruparClientesPorMes(clientes: Cliente[]): { [mesAnio: string]: Cliente[] } {
    const agrupados: { [mesAnio: string]: Cliente[] } = {};

    clientes.forEach(cliente => {
      if (cliente.fechaCreacion) {
        const partes = cliente.fechaCreacion.split('-');
        const anio = partes[0];
        const mes = parseInt(partes[1]);

        const nombreMes = this.obtenerNombreMes(mes);
        const clave = `${nombreMes} ${anio}`;

        if (!agrupados[clave]) {
          agrupados[clave] = [];
        }

        agrupados[clave].push(cliente);
      }
    });

    return agrupados;
  }

  obtenerNombreMes(mes: number): string {
    const meses = [
      'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
      'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ];
    return meses[mes - 1];
  }

  sortByDateDesc = (a: {key: string}, b: {key: string}) => {
    const getMonthYear = (str: string) => {
      const [mesStr, anioStr] = str.split(' ');
      const meses = [
        'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
      ];
      return new Date(parseInt(anioStr), meses.indexOf(mesStr));
    };
    return getMonthYear(b.key).getTime() - getMonthYear(a.key).getTime();
  };


  eliminarCliente(id: number): void {
    this.clienteService.eliminarCliente(id).subscribe(
      () => this.cargarClientes(),
      error => console.error(error)
    );
  }

  buscarCliente(searchValue: string): void {
    const cards = document.querySelectorAll('.cliente-card');
    let found = false;

    // Eliminar resaltados anteriores
    const resaltados = document.querySelectorAll('.resaltado');
    resaltados.forEach(span => {
      const parent = span.parentNode;
      if (parent) {
        parent.replaceChild(document.createTextNode(span.textContent || ''), span);
        parent.normalize();
      }
    });

    for (const card of cards) {
      const nombreElement = card.querySelector('.card-title');
      const dniElement = card.querySelector('p:nth-of-type(1) strong'); // primer <p> con <strong>

      const nombreText = nombreElement?.textContent?.toLowerCase() || '';
      const dniText = dniElement?.textContent?.toLowerCase() || '';

      if (nombreText.includes(searchValue) || dniText.includes(searchValue)) {
        this.highlightText(nombreElement as HTMLElement, searchValue);
        this.highlightText(dniElement?.parentElement as HTMLElement, searchValue);
        card.scrollIntoView({ behavior: "smooth", block: "center" });
        found = true;
        break; // Solo resalta el primer resultado
      }
    }

    if (!found) {
      alert("No se encontró ningún cliente con ese nombre o DNI.");
    }
  }

  highlightText(element: HTMLElement, searchValue: string) {
    const text = element.textContent || '';
    const regex = new RegExp(`(${searchValue})`, 'ig');
    const highlightedText = text.replace(regex, `<span class="resaltado">$1</span>`);
    element.innerHTML = highlightedText;
  }
}

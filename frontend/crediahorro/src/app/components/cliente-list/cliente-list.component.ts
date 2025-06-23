import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ClienteService, Cliente } from '../../services/cliente.service';
import { BusquedaService } from '../../services/busqueda.service';
import { Subscription } from 'rxjs';
import { NotificationService } from '../../services/notification.service';
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
    private busquedaService: BusquedaService,
    private notificationService: NotificationService
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
        hoyDate.setHours(0, 0, 0, 0); // Medianoche local

        clientes.forEach(cliente => {
          cliente.cuotaPendienteTexto = '';

          if (cliente.prestamos && cliente.prestamos.length > 0) {
            const prestamosOrdenados = [...cliente.prestamos].sort(
              (a, b) => new Date(b.fechaCreacion!).getTime() - new Date(a.fechaCreacion!).getTime()
            );

            const prestamoMasReciente = prestamosOrdenados[0];
            cliente.estadoPrestamoMasReciente = prestamoMasReciente.estado;

            if (prestamoMasReciente.cuotas) {
              const cuotasOrdenadas = [...prestamoMasReciente.cuotas].sort(
                (a, b) => new Date(a.fechaPago).getTime() - new Date(b.fechaPago).getTime()
              );

              // Cuota próxima
              const cuotaProxima = cuotasOrdenadas.find((cuota) => {
                if (cuota.estado !== 'PENDIENTE') return false;
                const fechaPagoDate = new Date(cuota.fechaPago + 'T00:00:00');
                fechaPagoDate.setHours(0, 0, 0, 0); // Medianoche
                const diffTime = fechaPagoDate.getTime() - hoyDate.getTime();
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                return diffDays >= 0 && diffDays <= 7;
              });

              if (cuotaProxima) {
                const fechaPagoDate = new Date(cuotaProxima.fechaPago + 'T00:00:00');
                fechaPagoDate.setHours(0, 0, 0, 0);
                const diffTime = fechaPagoDate.getTime() - hoyDate.getTime();
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                const index = cuotasOrdenadas.indexOf(cuotaProxima);

                cliente.cuotaPendienteTexto = diffDays === 0
                  ? `Hoy vence la ${this.ordinal(index + 1)} cuota`
                  : `En ${diffDays} día${diffDays === 1 ? '' : 's'} se vence la ${this.ordinal(index + 1)} cuota`;
              } else {
                // Cuota vencida
                const cuotaVencida = cuotasOrdenadas.find((cuota) => {
                  if (cuota.estado !== 'PENDIENTE') return false;
                  const fechaPagoDate = new Date(cuota.fechaPago + 'T00:00:00');
                  fechaPagoDate.setHours(0, 0, 0, 0);
                  return fechaPagoDate.getTime() < hoyDate.getTime();
                });

                if (cuotaVencida) {
                  const fechaPagoDate = new Date(cuotaVencida.fechaPago + 'T00:00:00');
                  fechaPagoDate.setHours(0, 0, 0, 0);
                  const diffTime = hoyDate.getTime() - fechaPagoDate.getTime();
                  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
                  const index = cuotasOrdenadas.indexOf(cuotaVencida);

                  cliente.cuotaPendienteTexto = `La ${this.ordinal(index + 1)} cuota venció hace ${diffDays} día${diffDays === 1 ? '' : 's'}`;
                } else {
                  const cuotasPagadas = cuotasOrdenadas.filter(c => c.estado === 'PAGADA');
                  if (cuotasPagadas.length > 0) {
                    const lastPagada = cuotasPagadas[cuotasPagadas.length - 1];
                    const indexLastPagada = cuotasOrdenadas.indexOf(lastPagada);

                    // solo mostramos si hay cuotas pendientes
                    const hayPendientes = cuotasOrdenadas.some(c => c.estado === 'PENDIENTE');
                    if (hayPendientes) {
                      cliente.cuotaPendienteTexto = `${this.ordinal(indexLastPagada + 1)} cuota pagada`;
                    }
                  }
                }
              }
            } else {
              cliente.estadoPrestamoMasReciente = 'SIN_PRESTAMO';
            }
          }
        });

        this.clientes = clientes;
        this.clientesAgrupadosPorMes = this.agruparClientesPorMes(clientes);
      },
      error => console.error(error)
    );
  }

  // Método para ordinales en español
    ordinal(n: number): string {
      const ordinals = ['1ra', '2da', '3ra', '4ta', '5ta', '6ta', '7ma', '8va', '9na', '10ma', '11va', '12va', '13va', '14va', '15va', '16va', '17va', '18va', '19va', '20va', '21va', '22va', '23va', '24va'];
      return ordinals[n - 1] || `${n}ta`;
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
      () => {
        this.notificationService.show('success', 'Cliente eliminado correctamente.');
        this.cargarClientes();
      },
      error => {
        this.notificationService.show('error', 'Error eliminando cliente.');
      }
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
      this.notificationService.show('error', 'No se encontró ningún cliente con ese nombre.');
    } else {
    this.notificationService.show('success', 'Se encontró cliente correctamente.');
    }
  }

  highlightText(element: HTMLElement, searchValue: string) {
    const text = element.textContent || '';
    const regex = new RegExp(`(${searchValue})`, 'ig');
    const highlightedText = text.replace(regex, `<span class="resaltado">$1</span>`);
    element.innerHTML = highlightedText;
  }
}

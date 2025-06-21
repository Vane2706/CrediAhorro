import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CuotaService, Cuota } from '../../services/cuota.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-cuota-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cuota-list.component.html',
  styleUrls: ['./cuota-list.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CuotaListComponent implements OnInit {
  cuotas: Cuota[] = [];
  prestamoId!: number;
  cuotasPendientes: number = 0;
  totalAPagar: number = 0;
  totalPagado: number = 0;
  faltaPagar: number = 0;
  mensajeVencimiento: string = '';
  showModal: boolean = false;
  cuotaSeleccionada: Cuota | null = null;

  constructor(
    private cuotaService: CuotaService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.prestamoId = +this.route.snapshot.paramMap.get('prestamoId')!;
    this.loadCuotas();
  }

  loadCuotas() {
    this.cuotaService.getCuotasByPrestamo(this.prestamoId).subscribe(data => {
      this.cuotas = data.cuotas;
      this.cuotasPendientes = data.cuotasPendientes;

      this.totalAPagar = this.cuotas.reduce((sum, c) => sum + c.montoCuota, 0);
      this.totalPagado = this.cuotas.filter(c => c.estado === 'PAGADA')
        .reduce((sum, c) => sum + c.montoCuota, 0);
      this.faltaPagar = this.cuotas.filter(c => c.estado === 'PENDIENTE')
        .reduce((sum, c) => sum + c.montoCuota, 0);
      this.mensajeVencimiento = '';
      const hoy = new Date();

      // Ordenamos las cuotas por fechaPago
      const cuotasOrdenadas = [...this.cuotas].sort((a, b) => new Date(a.fechaPago).getTime() - new Date(b.fechaPago).getTime());

      // Busca la primera pendiente en los próximos 7 días
      const cuotaProxima = cuotasOrdenadas.find((cuota) => {
        if (cuota.estado !== 'PENDIENTE') return false;
        const diffTime = new Date(cuota.fechaPago).getTime() - hoy.getTime();
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays >= 0 && diffDays <= 7;
      });

      if (cuotaProxima) {
        const diffTime = new Date(cuotaProxima.fechaPago).getTime() - hoy.getTime();
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        const index = cuotasOrdenadas.indexOf(cuotaProxima); // índice en cuotas ordenadas
        this.mensajeVencimiento = diffDays === 0
          ? `Hoy vence la ${this.ordinal(index + 1)} cuota`
          : `Falta ${diffDays} día${diffDays === 1 ? '' : 's'} para vencerse la ${this.ordinal(index + 1)} cuota`;
      } else {
        // Busca la primera vencida
        const cuotaVencida = cuotasOrdenadas.find((cuota) => {
          if (cuota.estado !== 'PENDIENTE') return false;
          return new Date(cuota.fechaPago).getTime() < hoy.getTime();
        });

        if (cuotaVencida) {
          const diffTime = hoy.getTime() - new Date(cuotaVencida.fechaPago).getTime();
          const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
          const index = cuotasOrdenadas.indexOf(cuotaVencida);
          this.mensajeVencimiento = `Venció la ${this.ordinal(index + 1)} cuota hace ${diffDays} día${diffDays === 1 ? '' : 's'}`;
        }
      }
    });
  }

  // Método para ordinales en español
  ordinal(n: number): string {
    const ordinals = ['1ra', '2da', '3ra', '4ta', '5ta', '6ta', '7ma', '8va', '9na', '10ma', '11va', '12va', '13va', '14va', '15va', '16va', '17va', '18va', '19va', '20va', '21va', '22va', '23va', '24va'];
    return ordinals[n - 1] || `${n}ta`;
  }

  pagarCuota(cuotaId: number) {
    this.cuotaService.pagarCuota(cuotaId).subscribe(() => {
      this.loadCuotas();
    });
  }

  irPagoAdelantado() {
    this.router.navigate(['/cuotas', this.prestamoId, 'pago-adelantado']);
  }

  volver() {
    this.router.navigate(['/clientes']);
  }

  abrirModal(cuota: Cuota): void {
    this.cuotaSeleccionada = cuota;
    this.showModal = true;
  }

  cerrarModal(): void {
    this.showModal = false;
    this.cuotaSeleccionada = null;
  }

  pagarAvanzado(tipo: string): void {
    if (!this.cuotaSeleccionada) return;

    this.cuotaService.pagarCuotaAvanzado(this.cuotaSeleccionada.id, tipo).subscribe(() => {
      this.loadCuotas();
      this.cerrarModal();
    });
  }
}

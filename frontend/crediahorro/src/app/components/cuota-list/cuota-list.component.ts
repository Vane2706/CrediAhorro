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
      this.totalPagado = this.cuotas
        .filter(c => c.estado === 'PAGADA')
        .reduce((sum, c) => sum + c.montoCuota, 0);
      this.faltaPagar = this.cuotas
        .filter(c => c.estado === 'PENDIENTE')
        .reduce((sum, c) => sum + c.montoCuota, 0);
    });
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

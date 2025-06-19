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
}

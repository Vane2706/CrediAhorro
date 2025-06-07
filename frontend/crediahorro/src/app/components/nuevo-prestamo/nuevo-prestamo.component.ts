import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PrestamoService, Prestamo } from '../../services/prestamo.service';

@Component({
  selector: 'app-nuevo-prestamo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nuevo-prestamo.component.html',
  styleUrls: ['./nuevo-prestamo.component.css']
})
export class NuevoPrestamoComponent {
  prestamo: Prestamo = {
    monto: 0,
    tasaInteresMensual: 0,
    numeroCuotas: 0,
    fechaInicio: ''
  };
  clienteId!: number;

  constructor(
    private prestamoService: PrestamoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.clienteId = Number(this.route.snapshot.paramMap.get('clienteId'));
  }

  guardarPrestamo() {
    this.prestamoService.crearPrestamo(this.clienteId, this.prestamo).subscribe({
      next: () => this.router.navigate(['/clientes/prestamos', this.clienteId]),
      error: (err: any) => console.error(err)
    });
  }

  cancelar() {
    this.router.navigate(['/clientes/prestamos', this.clienteId]);
  }
}

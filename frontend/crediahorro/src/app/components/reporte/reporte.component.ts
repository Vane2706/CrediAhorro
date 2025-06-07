import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../services/report.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reporte',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte.component.html',
  styleUrls: ['./reporte.component.css']
})
export class ReporteComponent implements OnInit {
  nombre: string = '';
  tipo: string = 'prestamos';
  estado: string = 'ACTIVO';
  estadosDisponibles: string[] = [];
  resultadoReporte: string | null = null;

  constructor(private reportService: ReportService) {}

  ngOnInit(): void {
    this.actualizarEstados();
  }

  actualizarEstados(): void {
    if (this.tipo === 'prestamos') {
      this.estadosDisponibles = ['ACTIVO', 'PAGADO'];
      this.estado = this.estadosDisponibles[0];
    } else if (this.tipo === 'cuotas') {
      this.estadosDisponibles = ['PENDIENTE', 'PAGADA'];
      this.estado = this.estadosDisponibles[0];
    }
  }

  generarReporte(): void {
    if (!this.nombre || !this.tipo || !this.estado) {
      alert('Completa todos los campos.');
      return;
    }

    this.reportService.generarReporte(this.nombre, this.tipo, this.estado)
      .subscribe({
        next: (data) => {
          if (!data || data.trim() === '') {
            this.resultadoReporte = '<div class="alert alert-warning">No hay datos existentes según su petición.</div>';
          } else {
            this.resultadoReporte = data;
          }
        },
        error: (error) => {
          console.error(error);
          alert('Error al generar el reporte.');
        }
      });
  }
}

import { Component, OnInit } from '@angular/core';
import { ReportGraficoService } from '../../services/report-grafico.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-dashboard-graficos',
  standalone: true,
  imports: [CommonModule, RouterModule, NgxChartsModule ],
  templateUrl: './dashboard-graficos.component.html',
  styleUrls: ['./dashboard-graficos.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DashboardGraficosComponent implements OnInit {
  anualConMesesData: any[] = [];

  view: [number, number] = [1200, 300];

  // opciones
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'Periodo';
  showYAxisLabel = true;
  yAxisLabel = 'Cantidad';

  colorScheme: any = {
    name: 'custom',
    selectable: true,
    group: 'Ordinal',
    domain: ['#7aa3e5', '#a8385d', '#aae3f5', '#A58ABF', '#E3B075', '#FFF293', '#C9DC92', '#E07C3E']
  };

  selectedData: any = null;
  showModal: boolean = false;
  constructor(private reportService: ReportGraficoService) {}

  ngOnInit(): void {
    this.reportService.getPorAnioConMeses().subscribe(data => {
      this.anualConMesesData = this.mapAnualConMesesToChart(data);
      console.log('Anual con meses:', this.anualConMesesData);
    });
  }

  mapAnualConMesesToChart(data: any): any[] {
    const [anio, mesesRaw] = Object.entries(data)[0];

    const meses = mesesRaw as Array<any>;  // Aquí aclaras el tipo

    return meses.map((m: any) => ({
      name: m.mes,
      value: m.montoPrestado,
      extra: {
        montoPrestado: m.montoPrestado,
        montoPagado: m.montoPagado,
        ganancia: m.ganancia,
        anio: anio
      }
    }));
  }

  onBarSelect(event: any): void {
    this.selectedData = event;
    this.showModal = true;
  }
}

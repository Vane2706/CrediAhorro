import { Component, OnInit } from '@angular/core';
import { ReportGraficoService } from '../../services/report-grafico.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgxChartsModule } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-dashboard-graficos',
  standalone: true,
  imports: [CommonModule, RouterModule, NgxChartsModule ],
  templateUrl: './dashboard-graficos.component.html',
  styleUrls: ['./dashboard-graficos.component.css']
})
export class DashboardGraficosComponent implements OnInit {
  semanalData: any[] = [];
  mensualData: any[] = [];
  anualData: any[] = [];

  view: [number, number] = [700, 300];

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
    domain: ['#7aa3e5', '#a8385d', '#aae3f5']
  };

  constructor(private reportService: ReportGraficoService) {}

  ngOnInit(): void {
    this.reportService.getSemanal().subscribe((data: any) => {
      console.log('Datos Semanal:', data);
      this.semanalData = this.mapToChartData(data);
      console.log('Datos transformados Semanal:', this.semanalData);
    });

    this.reportService.getMensual().subscribe((data: any) => {
      console.log('Datos Mensual:', data);
      this.mensualData = this.mapToChartData(data);
      console.log('Datos transformados Mensual:', this.mensualData);
    });

    this.reportService.getAnual().subscribe((data: any) => {
      console.log('Datos Anual:', data);
      this.anualData = this.mapToChartData(data);
      console.log('Datos transformados Anual:', this.anualData);
    });
  }


  mapToChartData(data: any): any[] {
    return Object.entries(data).map(([key, value]: [string, any]) => ({
      name: key,
      value: value['ACTIVO'] || 0
    }));
  }

}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportGraficoService {
  private apiUrl = 'http://localhost:4040/report-grafico/grafico/prestamos';

  constructor(private http: HttpClient) {}

  getSemanal(): Observable<any> {
    return this.http.get(`${this.apiUrl}?tipo=semanal`);
  }

  getMensual(): Observable<any> {
    return this.http.get(`${this.apiUrl}?tipo=mensual`);
  }

  getAnual(): Observable<any> {
    return this.http.get(`${this.apiUrl}?tipo=anual`);
  }
}

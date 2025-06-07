import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface Cuota {
  id: number;
  fechaPago: string;
  montoCuota: number;
  estado: string;
}

@Injectable({
  providedIn: 'root'
})
export class CuotaService {
  private baseUrl = `${environment.apiUrl}/admin-service/cuotas`;

  constructor(private http: HttpClient) {}

  getCuotasByPrestamo(prestamoId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/prestamo/${prestamoId}`);
  }

  pagarCuota(cuotaId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${cuotaId}/pagar`, {});
  }

  aplicarPagoAdelantado(prestamoId: number, monto: number, tipoReduccion: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/prestamos/${prestamoId}/pago-adelantado`, null, {
      params: {
        monto: monto.toString(),
        tipoReduccion
      }
    });
  }
}

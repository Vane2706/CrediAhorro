import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface Prestamo {
  id?: number;
  monto: number;
  tasaInteresMensual: number;
  numeroCuotas: number;
  fechaInicio: string;
  estado?: string;
  fechaCreacion?: string;
}

export interface Cliente {
  id?: number;
  nombre: string;
  dni: string;
  direccion: string;
  telefonoWhatsapp: string;
  correoElectronico: string;
  fechaCreacion?: string;
  prestamos?: Prestamo[];
}


@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private baseUrl = `${environment.apiUrl}/admin-service/clientes`;

  constructor(private http: HttpClient) { }

  getClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.baseUrl);
  }

  crearCliente(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.baseUrl, cliente);
  }

  getClienteById(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.baseUrl}/${id}`);
  }

  updateCliente(id: number, cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.baseUrl}/${id}`, cliente);
  }

  eliminarCliente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

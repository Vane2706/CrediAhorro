package upeu.edu.pe.admin_core_service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NotificacionDto {
    private String telefono;
    private String nombre;
    private String tipoMensaje; // por ejemplo: "RECORDATORIO" o "ALERTA"
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaPago;
    private String monto_cuota;

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getMonto_cuota() {
        return monto_cuota;
    }

    public void setMonto_cuota(String monto_cuota) {
        this.monto_cuota = monto_cuota;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    @Override
    public String toString() {
        return "NotificacionDto{" +
                "telefono='" + telefono + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoMensaje='" + tipoMensaje + '\'' +
                ", fechaPago=" + fechaPago +
                ", monto_cuota='" + monto_cuota + '\'' +
                '}';
    }
}

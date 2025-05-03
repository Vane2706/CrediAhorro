package upeu.edu.pe.notificacion_service.dto;

import java.time.LocalDate;

public class PrestamoResumenDTO {
    private double monto;
    private double cuotaMensual;
    private int meses;
    private LocalDate fechaInicio;

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(double cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getMeses() {
        return meses;
    }

    public void setMeses(int meses) {
        this.meses = meses;
    }
}

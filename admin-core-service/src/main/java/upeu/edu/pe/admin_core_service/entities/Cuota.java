package upeu.edu.pe.admin_core_service.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cuotas")
public class Cuota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaPago;
    private double montoCuota;
    private String estado; // PENDIENTE o PAGADA

    public Cuota() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(double montoCuota) {
        this.montoCuota = montoCuota;
    }

    @Override
    public String toString() {
        return "Cuota{" +
                "id=" + id +
                ", fechaPago=" + fechaPago +
                ", montoCuota=" + montoCuota +
                ", estado='" + estado + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cuota cuota = (Cuota) o;
        return Double.compare(montoCuota, cuota.montoCuota) == 0 && Objects.equals(id, cuota.id) && Objects.equals(fechaPago, cuota.fechaPago) && Objects.equals(estado, cuota.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaPago, montoCuota, estado);
    }
}
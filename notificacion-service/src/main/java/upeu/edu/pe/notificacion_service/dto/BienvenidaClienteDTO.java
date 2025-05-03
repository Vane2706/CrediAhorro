package upeu.edu.pe.notificacion_service.dto;

import java.util.List;

public class BienvenidaClienteDTO {
    private String nombreCliente;
    private String numero;
    private List<PrestamoResumenDTO> prestamos;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<PrestamoResumenDTO> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<PrestamoResumenDTO> prestamos) {
        this.prestamos = prestamos;
    }
}

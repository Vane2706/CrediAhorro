package upeu.edu.pe.historial_service.dto;

import lombok.AllArgsConstructor;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    private Long id;
    private String nombre;
    private String dni;
    private String direccion;
    private String telefonoWhatsapp;
    private String correoElectronico;
    private List<Prestamo> prestamos;
}

package upeu.edu.pe.historial_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestamo {

    private Long id;
    private double monto;
    private double tasaInteresMensual;
    private int numeroCuotas;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;
    private String estado; // ACTIVO o PAGADO
    private List<Cuota> cuotas;
}
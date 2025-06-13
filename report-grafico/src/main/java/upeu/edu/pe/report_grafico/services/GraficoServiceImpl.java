package upeu.edu.pe.report_grafico.services;

import org.springframework.stereotype.Service;
import upeu.edu.pe.report_grafico.models.Prestamo;
import upeu.edu.pe.report_grafico.repositories.PrestamoRepository;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraficoServiceImpl implements GraficoService {

    private final PrestamoRepository prestamoRepository;

    public GraficoServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Map<String, Map<String, Double>> resumenPorPeriodo(String tipo) {
        List<Prestamo> prestamos = prestamoRepository.obtenerTodosLosPrestamos();

        return prestamos.stream()
                .collect(Collectors.groupingBy(
                        p -> agruparPorPeriodo(p.getFechaCreacion(), tipo),
                        Collectors.groupingBy(
                                Prestamo::getEstado,
                                Collectors.summingDouble(Prestamo::getMonto)
                        )
                ));
    }

    private String agruparPorPeriodo(LocalDate fecha, String tipo) {
        switch (tipo.toLowerCase()) {
            case "semanal":
                WeekFields wf = WeekFields.of(Locale.getDefault());
                return "Semana " + fecha.get(wf.weekOfWeekBasedYear()) + " - " + fecha.getYear();
            case "mensual":
                return fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")) + " - " + fecha.getYear();
            case "anual":
                return String.valueOf(fecha.getYear());
            default:
                return "Desconocido";
        }
    }
}

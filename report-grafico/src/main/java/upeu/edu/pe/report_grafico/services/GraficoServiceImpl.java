package upeu.edu.pe.report_grafico.services;

import org.springframework.stereotype.Service;
import upeu.edu.pe.report_grafico.models.Cuota;
import upeu.edu.pe.report_grafico.models.Prestamo;
import upeu.edu.pe.report_grafico.repositories.PrestamoRepository;

import java.time.LocalDate;
import java.time.Month;
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
    public Map<String, List<Map<String, Object>>> resumenPorAnioConMeses() {
        List<Prestamo> prestamos = prestamoRepository.obtenerTodosLosPrestamos();

        Map<String, List<Map<String, Object>>> resultado = new TreeMap<>(); // TreeMap para que se ordene por año

        // Inicializar los 12 meses para cada año
        Set<Integer> anios = prestamos.stream()
                .map(p -> p.getFechaCreacion().getYear())
                .collect(Collectors.toSet());

        for (Integer anio : anios) {
            List<Map<String, Object>> meses = new ArrayList<>();
            for (Month mes : Month.values()) {
                Map<String, Object> mesData = new HashMap<>();
                mesData.put("mes", mes.getDisplayName(TextStyle.FULL, new Locale("es")));
                mesData.put("montoPrestado", 0.0);
                mesData.put("montoPagado", 0.0);
                mesData.put("ganancia", 0.0);
                meses.add(mesData);
            }
            resultado.put(String.valueOf(anio), meses);
        }

        for (Prestamo prestamo : prestamos) {
            String anio = String.valueOf(prestamo.getFechaCreacion().getYear());
            String mesNombre = prestamo.getFechaCreacion().getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));

            double montoPrestado = prestamo.getMonto();
            double montoPagado = prestamo.getCuotas().stream()
                    .filter(c -> "PAGADA".equalsIgnoreCase(c.getEstado()))
                    .mapToDouble(Cuota::getMontoCuota)
                    .sum();

            List<Map<String, Object>> meses = resultado.get(anio);

            Map<String, Object> mesData = meses.stream()
                    .filter(m -> m.get("mes").equals(mesNombre))
                    .findFirst()
                    .orElseThrow();

            mesData.put("montoPrestado", ((Number) mesData.get("montoPrestado")).doubleValue() + montoPrestado);
            mesData.put("montoPagado", ((Number) mesData.get("montoPagado")).doubleValue() + montoPagado);
            double ganancia = ((Number) mesData.get("montoPagado")).doubleValue() - ((Number) mesData.get("montoPrestado")).doubleValue();
            mesData.put("ganancia", ganancia);
        }

        return resultado;
    }
}

package upeu.edu.pe.report_grafico.services;

import java.util.Map;

public interface GraficoService {
    Map<String, Map<String, Double>> resumenPorPeriodo(String tipo);
}

package upeu.edu.pe.report_ms.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.report_ms.services.ReportService;

import java.util.Map;

@RestController
@RequestMapping(path = "report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(path = "/prestamos")
    public ResponseEntity<Map<String, String>> obtenerReportePrestamos(@RequestParam Long clienteId,
                                                                       @RequestParam String estado) {
        String reporte = reportService.generarReportePrestamos(clienteId, estado);
        return ResponseEntity.ok(Map.of("reporte", reporte));
    }

    @GetMapping(path = "/cuotas")
    public ResponseEntity<Map<String, String>> obtenerReporteCuotas(@RequestParam Long clienteId,
                                                                    @RequestParam String estado) {
        String reporte = reportService.generarReporteCuotas(clienteId, estado);
        return ResponseEntity.ok(Map.of("reporte", reporte));
    }
}

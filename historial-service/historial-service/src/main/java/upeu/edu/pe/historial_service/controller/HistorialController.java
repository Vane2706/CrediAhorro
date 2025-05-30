package upeu.edu.pe.historial_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.historial_service.dto.Cuota;
import upeu.edu.pe.historial_service.dto.Prestamo;
import upeu.edu.pe.historial_service.service.HistorialService;

import java.util.List;

@RestController
@RequestMapping(path = "historial")
@Tag(name = "Historial de Créditos")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @Operation(summary = "Listar préstamos por cliente y estado")
    @GetMapping(path = "/prestamos")
    public ResponseEntity<List<Prestamo>> prestamosPorEstado(@RequestParam Long clienteId, @RequestParam String estado) {
        return ResponseEntity.ok(historialService.listarPrestamosPorEstado(clienteId, estado));
    }

    @Operation(summary = "Listar cuotas por cliente y estado")
    @GetMapping(path = "/cuotas")
    public ResponseEntity<List<Cuota>> cuotasPorEstado(@RequestParam Long clienteId, @RequestParam String estado) {
        return ResponseEntity.ok(historialService.listarCuotasPorEstado(clienteId, estado));
    }
}

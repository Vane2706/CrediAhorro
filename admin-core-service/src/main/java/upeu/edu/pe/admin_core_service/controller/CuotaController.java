package upeu.edu.pe.admin_core_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.admin_core_service.entities.Cuota;
import upeu.edu.pe.admin_core_service.entities.Prestamo;
 import upeu.edu.pe.admin_core_service.repository.PrestamoRepository;
import upeu.edu.pe.admin_core_service.service.CuotaService;
import upeu.edu.pe.admin_core_service.service.PagoAdelantadoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cuotas")
@Tag(name = "Cuotas Resource")
public class CuotaController {

    private PrestamoRepository prestamoRepository;
    private final CuotaService cuotaService;
    private final PagoAdelantadoService pagoAdelantadoService;

    public CuotaController(PrestamoRepository prestamoRepository,
                           CuotaService cuotaService,
                           PagoAdelantadoService pagoAdelantadoService) {
        this.prestamoRepository = prestamoRepository;
        this.cuotaService = cuotaService;
        this.pagoAdelantadoService = pagoAdelantadoService;
    }

    @Operation(summary = "Buscar cuotas existente con el id del prestamo")
    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<List<Cuota>> obtenerCuotasPorPrestamo(@PathVariable Long prestamoId) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(prestamoId);
        if (prestamo.isPresent()) {
            List<Cuota> cuotas = prestamo.get().getCuotas(); // <-- relación OneToMany
            return ResponseEntity.ok(cuotas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Pagar una cuota")
    @PutMapping("/{cuotaId}/pagar")
    public ResponseEntity<Cuota> pagarCuota(@PathVariable Long cuotaId) {
        Cuota cuotaPagada = cuotaService.pagarCuota(cuotaId);
        return ResponseEntity.ok(cuotaPagada);
    }

    @PostMapping("/prestamos/{id}/pago-adelantado")
    public ResponseEntity<Prestamo> aplicarPagoAdelantado(
            @PathVariable Long id,
            @RequestParam double monto,
            @RequestParam String tipoReduccion
    ) {
        Prestamo prestamo = pagoAdelantadoService.aplicarPagoAdelantado(id, monto, tipoReduccion);
        return ResponseEntity.ok(prestamo);
    }

}

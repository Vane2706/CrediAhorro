package upeu.edu.pe.admin_core_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upeu.edu.pe.admin_core_service.entities.Cuota;
import upeu.edu.pe.admin_core_service.entities.Prestamo;
 import upeu.edu.pe.admin_core_service.repository.PrestamoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cuotas")
@Tag(name = "Cuotas Resource")
public class CuotaController {

    private PrestamoRepository prestamoRepository;

    public CuotaController(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
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
}

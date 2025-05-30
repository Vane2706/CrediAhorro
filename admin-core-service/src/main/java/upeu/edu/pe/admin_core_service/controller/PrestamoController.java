package upeu.edu.pe.admin_core_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.admin_core_service.entities.Prestamo;
import upeu.edu.pe.admin_core_service.service.PrestamoService;

@RestController
@RequestMapping(path = "prestamos")
@Tag(name = "Prestamos Resource")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @Operation(summary = "Buscar prestamo existente")
    @GetMapping(path = "{id}")
    public ResponseEntity<Prestamo> obtenerPorId(@PathVariable Long id) {
        return prestamoService.obtenerPrestamoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo prestamo")
    @PostMapping(path = "/cliente/{clienteId}")
    public ResponseEntity<Prestamo> crearPrestamoParaCliente(@PathVariable Long clienteId, @RequestBody Prestamo prestamo) {
        return ResponseEntity.ok(prestamoService.crearPrestamoParaCliente(clienteId, prestamo));
    }

    @Operation(summary = "Actulizar prestamo existente")
    @PutMapping(path = "{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(@PathVariable Long id, @RequestBody Prestamo nuevoPrestamo) {
        return ResponseEntity.ok(prestamoService.actualizarPrestamo(id, nuevoPrestamo));
    }

    @Operation(summary = "Eliminar prestamo existente")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return ResponseEntity.noContent().build();
    }
}

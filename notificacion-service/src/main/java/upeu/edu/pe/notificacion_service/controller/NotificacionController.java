package upeu.edu.pe.notificacion_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.notificacion_service.dto.BienvenidaClienteDTO;
import upeu.edu.pe.notificacion_service.dto.NotificacionAdminDTO;
import upeu.edu.pe.notificacion_service.dto.NotificacionClienteDTO;
import upeu.edu.pe.notificacion_service.service.NotificacionService;

import java.time.LocalDate;

@RestController
@RequestMapping("/notificar")
@Tag(name = "Notificaciones Resource", description = "Operaciones para enviar notificaciones a clientes y administrador")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Enviar código de verificación al administrador")
    @PostMapping("/admin")
    public ResponseEntity<Void> notificarAdmin(@RequestBody NotificacionAdminDTO dto) {
        notificacionService.enviarCodigoAdministrador(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Enviar mensaje de bienvenida a nuevo cliente")
    @PostMapping("/cliente/bienvenida-nuevo")
    public ResponseEntity<Void> bienvenidaClienteNuevo(@RequestBody BienvenidaClienteDTO dto) {
        notificacionService.enviarBienvenidaClienteNuevo(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Notificar nuevo préstamo a cliente")
    @PostMapping("/cliente/nuevo-prestamo")
    public ResponseEntity<Void> nuevoPrestamo(@RequestBody NotificacionClienteDTO dto) {
        notificacionService.enviarMensajeNuevoPrestamo(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Enviar recordatorio anticipado de pago al cliente")
    @PostMapping("/cliente/recordatorio-anticipado")
    public ResponseEntity<Void> recordatorioAnticipado(
            @RequestParam String numero,
            @RequestParam String nombre,
            @RequestParam String fecha,
            @RequestParam double monto,
            @RequestParam int cuota
    ) {
        notificacionService.enviarRecordatorioAnticipado(numero, nombre, LocalDate.parse(fecha), monto, cuota);
        return ResponseEntity.ok().build();
    }
}
package upeu.edu.pe.notificacion_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.notificacion_service.dto.NotificacionAdminDTO;
import upeu.edu.pe.notificacion_service.dto.NotificacionClienteDTO;
import upeu.edu.pe.notificacion_service.service.NotificacionService;

import java.time.LocalDate;

@RestController
@RequestMapping("/notificar")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping("/admin")
    public void notificarAdmin(@RequestBody NotificacionAdminDTO dto) {
        notificacionService.enviarCodigoAdministrador(dto);
    }

    @PostMapping("/cliente")
    public void notificarCliente(@RequestBody NotificacionClienteDTO dto) {
        notificacionService.enviarMensajeBienvenida(dto);
    }

    @PostMapping("/cliente/recordatorio")
    public void recordatorioPago(
            @RequestParam String numero,
            @RequestParam String nombre,
            @RequestParam String fecha,
            @RequestParam double monto,
            @RequestParam boolean hoy
    ) {
        notificacionService.enviarRecordatorioPago(numero, nombre, LocalDate.parse(fecha), monto, hoy);
    }
}
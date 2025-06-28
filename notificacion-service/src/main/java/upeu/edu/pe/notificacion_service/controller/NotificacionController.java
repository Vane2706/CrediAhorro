package upeu.edu.pe.notificacion_service.controller;

import upeu.edu.pe.notificacion_service.dto.NotificacionDto;
import upeu.edu.pe.notificacion_service.service.TwilioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificar")
public class NotificacionController {

    private final TwilioService twilioService;

    public NotificacionController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    // NotificacionController.java
    @PostMapping("/prestamo")
    public void notificarPrestamo(@RequestBody NotificacionDto dto) {
        String mensaje = "Hola " + dto.getNombre() + ", tu préstamo ha sido generado exitosamente.";
        twilioService.enviarMensaje(dto.getTelefono(), mensaje);
    }

    @PostMapping("/recordatorio")
    public void notificarRecordatorio(@RequestBody NotificacionDto dto) {
        String mensaje = "Hola " + dto.getNombre()+ ", te recordamos que tu pago vence el " + dto.getFechaPago()+ ". ¡Evita intereses!";
        twilioService.enviarMensaje(dto.getTelefono(), mensaje);
    }

    @PostMapping("/advertencia")
    public void notificarAdvertencia(@RequestBody NotificacionDto dto) {
        String mensaje = "Hola " + dto.getNombre()+ ", te recordamos que tu cuota de" +dto.getMonto_cuota()+"vencio" +", el día"+ dto.getFechaPago();
        twilioService.enviarMensaje(dto.getTelefono(), mensaje);
    }
}
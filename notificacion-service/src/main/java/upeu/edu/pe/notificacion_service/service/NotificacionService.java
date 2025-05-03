package upeu.edu.pe.notificacion_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import upeu.edu.pe.notificacion_service.dto.BienvenidaClienteDTO;
import upeu.edu.pe.notificacion_service.dto.NotificacionAdminDTO;
import upeu.edu.pe.notificacion_service.dto.NotificacionClienteDTO;
import upeu.edu.pe.notificacion_service.dto.PrestamoResumenDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificacionService {

    private final RestTemplate restTemplate;

    @Value("${whatsapp.api-url}")
    private String apiUrl;

    @Value("${whatsapp.token}")
    private String token;

    public NotificacionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void enviarCodigoAdministrador(NotificacionAdminDTO dto) {
        String mensaje = "¡Hola " + dto.getNombre() + "! Tu código de autenticación es: *" + dto.getCodigo() + "*";

        enviarMensajeWhatsApp(dto.getNumero(), mensaje);
    }

    public void enviarBienvenidaClienteNuevo(BienvenidaClienteDTO dto) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡Bienvenido a *CrediAhorro*, ").append(dto.getNombreCliente()).append("! 🎉\n")
                .append("Estos son los detalles de tus préstamos:\n\n");

        int i = 1;
        for (PrestamoResumenDTO p : dto.getPrestamos()) {
            mensaje.append("*Préstamo ").append(i++).append(":*\n")
                    .append("Monto: S/. ").append(p.getMonto()).append("\n")
                    .append("Cuota mensual: S/. ").append(p.getCuotaMensual()).append("\n")
                    .append("Meses: ").append(p.getMeses()).append("\n")
                    .append("Inicio: ").append(p.getFechaInicio()).append("\n\n");
        }

        mensaje.append("¡Gracias por confiar en nosotros! 💚");

        enviarMensajeWhatsApp(dto.getNumero(), mensaje.toString());
    }

    public void enviarMensajeNuevoPrestamo(NotificacionClienteDTO dto) {
        String mensaje = "Hola *" + dto.getNombreCliente() + "* 👋\n" +
                "Gracias por confiar nuevamente en *CrediAhorro* 💚.\n" +
                "Se ha registrado un nuevo préstamo:\n\n" +
                "*Monto:* S/. " + dto.getMonto() + "\n" +
                "*Cuotas:* " + dto.getMeses() + "\n" +
                "*Monto por cuota:* S/. " + dto.getCuotaMensual() + "\n" +
                "*Inicio:* " + dto.getFechaInicio() + "\n\n" +
                "¡Te deseamos éxito en tus proyectos! 💪";

        enviarMensajeWhatsApp(dto.getNumero(), mensaje);
    }



    public void enviarRecordatorioAnticipado(String numero, String nombre, LocalDate fechaPago, double monto, int numeroCuota) {
        String mensaje = "Hola *" + nombre + "* 👋\n" +
                "⏳ ¡Tu *" + numeroCuota + "° cuota* está por vencer!\n" +
                "Fecha límite: *" + fechaPago + "*\n" +
                "Monto: *S/. " + monto + "*\n\n" +
                "Te recordamos que pagar puntualmente mantiene tus beneficios activos. 💚";

        enviarMensajeWhatsApp(numero, mensaje);
    }

    public void enviarRecordatorioHoy(String numero, String nombre, LocalDate fechaPago, double monto, int numeroCuota) {
        String mensaje = "📅 *¡Hoy es el día, " + nombre + "!*\n" +
                "Debes pagar tu *" + numeroCuota + "° cuota*:\n" +
                "Monto: *S/. " + monto + "*\n" +
                "Fecha: *" + fechaPago + "*\n\n" +
                "¡Gracias por tu puntualidad! 💵";

        enviarMensajeWhatsApp(numero, mensaje);
    }

    public void enviarConfirmacionPagoCuota(String numero, String nombre, int numeroCuota, double monto) {
        String mensaje = "🎉 ¡Gracias " + nombre + "!\n" +
                "Hemos recibido el pago de tu *" + numeroCuota + "° cuota* por *S/. " + monto + "*.\n" +
                "¡Tu compromiso es importante para nosotros! 💚";

        enviarMensajeWhatsApp(numero, mensaje);
    }

    public void enviarFelicitacionFinPrestamo(String numero, String nombre) {
        String mensaje = "🎊 ¡Felicidades " + nombre + "!\n" +
                "Has terminado de pagar tu préstamo con éxito. 🎉\n" +
                "Gracias por confiar en *CrediAhorro*.\n" +
                "Esperamos poder ayudarte nuevamente pronto. 💵💚";

        enviarMensajeWhatsApp(numero, mensaje);
    }

    private void enviarMensajeWhatsApp(String numeroDestino, String mensaje) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("messaging_product", "whatsapp");
        payload.put("to", numeroDestino);
        payload.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("preview_url", "false");
        text.put("body", mensaje);

        payload.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
    }
}
package upeu.edu.pe.notificacion_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import upeu.edu.pe.notificacion_service.dto.NotificacionAdminDTO;
import upeu.edu.pe.notificacion_service.dto.NotificacionClienteDTO;

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

    public void enviarMensajeBienvenida(NotificacionClienteDTO dto) {
        String mensaje = "Hola *" + dto.getNombreCliente() + "* 👋\n" +
                "Tu préstamo fue registrado con éxito.\n" +
                "*Monto:* S/. " + dto.getMonto() + "\n" +
                "*Cuotas:* " + dto.getMeses() + "\n" +
                "*Monto por cuota:* S/. " + dto.getCuotaMensual() + "\n" +
                "*Inicio:* " + dto.getFechaInicio();

        enviarMensajeWhatsApp(dto.getNumero(), mensaje);
    }

    public void enviarRecordatorioPago(String numero, String nombre, LocalDate fechaPago, double monto, boolean esHoy) {
        String mensaje = "Hola *" + nombre + "* 👋\n" +
                (esHoy ? "Hoy debes realizar tu pago. " : "Recuerda que en 5 días debes pagar.") +
                "\nFecha: *" + fechaPago + "*\nMonto: *S/. " + monto + "*";

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
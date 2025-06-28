package upeu.edu.pe.admin_core_service.configs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import upeu.edu.pe.admin_core_service.dto.NotificacionDto;
@FeignClient(name = "notificacion-service")
public interface NotificacionClient {

    @PostMapping("/api/notificar/prestamo")
    void notificarPrestamo(@RequestBody NotificacionDto dto);

    @PostMapping("/api/notificar/recordatorio")
    void notificarRecordatorio(@RequestBody NotificacionDto dto);
    @PostMapping("/api/notificar/advertencia")
    void notificarAdvertencia(@RequestBody NotificacionDto dto);
}

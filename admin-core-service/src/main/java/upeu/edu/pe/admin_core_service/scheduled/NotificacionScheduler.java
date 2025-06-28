package upeu.edu.pe.admin_core_service.scheduled;

 ;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import upeu.edu.pe.admin_core_service.configs.NotificacionClient;
import upeu.edu.pe.admin_core_service.dto.NotificacionDto;
import upeu.edu.pe.admin_core_service.service.CuotaService;

import java.time.LocalDate;
import java.util.List;

@Component

public class NotificacionScheduler {

    private final NotificacionClient notificacionClient;
    private final CuotaService cuotaService;

    public NotificacionScheduler(NotificacionClient notificacionClient, CuotaService cuotaService) {
        this.notificacionClient = notificacionClient;
        this.cuotaService = cuotaService;
    }

    /**
     * Enviar recordatorios diariamente a las 8:00 AM
     */

    @Scheduled(cron = "0 05 12 * * *") // cada día a las 08:00 AM
    public void enviarRecordatorios() {
        List<NotificacionDto> notificaciones = cuotaService.obtenerCuotasPorVencer();

        for (NotificacionDto dto : notificaciones) {
            dto.setTipoMensaje("RECORDATORIO");
            notificacionClient.notificarRecordatorio(dto);
        }
    }


    /**
     * Enviar advertencias diariamente a las 9:00 AM
     */
    // Enviar advertencias diarias
    @Scheduled(cron = "0 0 9 * * *") // cada día a las 09:00 AM
    public void enviarAdvertencias() {
        List<NotificacionDto> notificaciones = cuotaService.obtenerCuotasVencidas();

        for (NotificacionDto dto : notificaciones) {
            dto.setTipoMensaje("ADVERTENCIA");
            notificacionClient.notificarAdvertencia(dto);
        }
    }

}

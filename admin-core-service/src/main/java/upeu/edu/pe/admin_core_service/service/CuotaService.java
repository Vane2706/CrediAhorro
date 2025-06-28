package upeu.edu.pe.admin_core_service.service;

import org.springframework.data.jpa.repository.EntityGraph;
import upeu.edu.pe.admin_core_service.dto.NotificacionDto;
import upeu.edu.pe.admin_core_service.entities.Cuota;

import java.time.LocalDate;
import java.util.List;

public interface CuotaService {
    Long findPrestamoIdByCuotaId(Long cuotaId);

    Cuota pagarCuota(Long cuotaId);

    Cuota pagarCuotaAvanzado(Long cuotaId, String tipoPago);

    List<NotificacionDto> obtenerCuotasPorVencer();
    List<NotificacionDto> obtenerCuotasVencidas();
}


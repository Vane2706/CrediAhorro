package upeu.edu.pe.admin_core_service.service;

import upeu.edu.pe.admin_core_service.entities.Cuota;

public interface CuotaService {
    Long findPrestamoIdByCuotaId(Long cuotaId);
    Cuota pagarCuota(Long cuotaId);
}

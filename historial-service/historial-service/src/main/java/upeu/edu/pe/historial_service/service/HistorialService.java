package upeu.edu.pe.historial_service.service;

import upeu.edu.pe.historial_service.dto.Cuota;
import upeu.edu.pe.historial_service.dto.Prestamo;

import java.util.List;

public interface HistorialService {
    List<Prestamo> listarPrestamosPorEstado(Long clienteId, String estado);
    List<Cuota> listarCuotasPorEstado(Long clienteId, String estado);
}


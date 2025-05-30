package upeu.edu.pe.historial_service.service;

import org.springframework.stereotype.Service;
import upeu.edu.pe.historial_service.dto.Cuota;
import upeu.edu.pe.historial_service.dto.Prestamo;
import upeu.edu.pe.historial_service.repository.AdminCoreClient;

import java.util.List;

@Service
public class HistorialServiceImpl implements HistorialService {

    private final AdminCoreClient adminCoreClient;

    public HistorialServiceImpl(AdminCoreClient adminCoreClient) {
        this.adminCoreClient = adminCoreClient;
    }

    @Override
    public List<Prestamo> listarPrestamosPorEstado(Long clienteId, String estado) {
        return adminCoreClient.obtenerPrestamos(clienteId, estado);
    }

    @Override
    public List<Cuota> listarCuotasPorEstado(Long clienteId, String estado) {
        return adminCoreClient.obtenerCuotas(clienteId, estado);
    }
}


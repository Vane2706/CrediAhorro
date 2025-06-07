package upeu.edu.pe.admin_core_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import upeu.edu.pe.admin_core_service.entities.Cuota;
import upeu.edu.pe.admin_core_service.entities.Prestamo;
import upeu.edu.pe.admin_core_service.repository.CuotaRepository;
import upeu.edu.pe.admin_core_service.repository.PrestamoRepository;

import java.util.Optional;

@Service
@Transactional
public class CuotaServiceImpl implements CuotaService {

    private final CuotaRepository cuotaRepository;
    private final PrestamoRepository prestamoRepository;

    public CuotaServiceImpl(CuotaRepository cuotaRepository, PrestamoRepository prestamoRepository) {
        this.cuotaRepository = cuotaRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Cuota pagarCuota(Long cuotaId) {
        Cuota cuota = cuotaRepository.findById(cuotaId)
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));

        cuota.setEstado("PAGADA");
        cuotaRepository.save(cuota);

        // Revisar si todas las cuotas del préstamo están pagadas
        actualizarEstadoPrestamoSiEsNecesario(cuota);

        return cuota;
    }

    private void actualizarEstadoPrestamoSiEsNecesario(Cuota cuota) {
        // Buscar el préstamo relacionado
        Optional<Prestamo> prestamoOpt = prestamoRepository.findAll().stream()
                .filter(p -> p.getCuotas().stream().anyMatch(c -> c.getId().equals(cuota.getId())))
                .findFirst();

        if (prestamoOpt.isPresent()) {
            Prestamo prestamo = prestamoOpt.get();

            boolean todasPagadasOAdelantadas = prestamo.getCuotas().stream()
                    .allMatch(c -> {
                        String estado = c.getEstado().toUpperCase();
                        return estado.equals("PAGADA") || estado.equals("ADELANTADO");
                    });

            if (todasPagadasOAdelantadas) {
                prestamo.setEstado("PAGADO");
                prestamoRepository.save(prestamo);
            }
        }
    }

    @Override
    public Long findPrestamoIdByCuotaId(Long cuotaId) {
        return cuotaRepository.findPrestamoIdByCuotaId(cuotaId);
    }
}

package upeu.edu.pe.admin_core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.admin_core_service.entities.Cuota;

import java.util.List;

public interface CuotaRepository extends JpaRepository<Cuota, Long> {
}

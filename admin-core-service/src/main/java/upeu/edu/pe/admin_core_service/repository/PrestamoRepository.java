package upeu.edu.pe.admin_core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.admin_core_service.entities.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}

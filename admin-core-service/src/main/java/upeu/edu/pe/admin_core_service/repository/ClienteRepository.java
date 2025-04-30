package upeu.edu.pe.admin_core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.admin_core_service.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

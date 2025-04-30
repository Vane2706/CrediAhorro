package upeu.edu.pe.admin_core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import upeu.edu.pe.admin_core_service.entities.Prestamo;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    @Query("SELECT p FROM Prestamo p JOIN Cliente c ON p.id IN " +
            "(SELECT pr.id FROM Cliente cl JOIN cl.prestamos pr WHERE cl.id = :clienteId) " +
            "WHERE p.estado = :estado")
    List<Prestamo> findPrestamosByClienteIdAndEstado(@Param("clienteId") Long clienteId,
                                                     @Param("estado") String estado);

}

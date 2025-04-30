package upeu.edu.pe.admin_core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import upeu.edu.pe.admin_core_service.entities.Cuota;

import java.util.List;

public interface CuotaRepository extends JpaRepository<Cuota, Long> {
    @Query("SELECT c FROM Cuota c WHERE c.id IN (" +
            "SELECT cu.id FROM Prestamo p JOIN p.cuotas cu " +
            "WHERE p.id IN (SELECT pr.id FROM Cliente cl JOIN cl.prestamos pr WHERE cl.id = :clienteId)) " +
            "AND c.estado = :estado")
    List<Cuota> findCuotasByClienteIdAndEstado(@Param("clienteId") Long clienteId,
                                               @Param("estado") String estado);

}

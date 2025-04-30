package upeu.edu.pe.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.auth_service.entities.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}
package upeu.edu.pe.auth_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import upeu.edu.pe.auth_service.entities.Admin;
import upeu.edu.pe.auth_service.repository.AdminRepository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{

    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin register(Admin admin) {
        if (adminRepository.count() == 0) {
            admin.setCodigoAutenticacion(UUID.randomUUID().toString().substring(0,6)); // Código único
            // Aquí llamarías al servicio de WhatsApp para enviar el código
        } else {
            admin.setCodigoAutenticacion(null); // Otros registros no reciben código
        }

        // Encriptar la contraseña antes de guardarla
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return adminRepository.save(admin);
    }

    @Override
    public boolean login(String username, String password, String codigo) {
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            return passwordEncoder.matches(password, admin.getPassword()) && // Verifica la contraseña
                    (admin.getCodigoAutenticacion() == null || admin.getCodigoAutenticacion().equals(codigo));
        }
        return false;
    }
}

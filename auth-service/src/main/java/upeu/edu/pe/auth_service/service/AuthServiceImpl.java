package upeu.edu.pe.auth_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import upeu.edu.pe.auth_service.entities.Admin;
import upeu.edu.pe.auth_service.repository.AdminRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{

    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public AuthServiceImpl(AdminRepository adminRepository,
                           PasswordEncoder passwordEncoder,
                           RestTemplate restTemplate) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @Override
    public Admin register(Admin admin) {
        boolean esPrimerAdmin = adminRepository.count() == 0;

        if (esPrimerAdmin) {
            String codigo = UUID.randomUUID().toString().substring(0, 6);
            admin.setCodigoAutenticacion(codigo);

            // Enviar código al WhatsApp del administrador
            Map<String, String> body = new HashMap<>();
            body.put("nombre", admin.getUsername());
            body.put("numero", admin.getTelefonoWhatsapp());
            body.put("codigo", codigo);

            String notificacionUrl = "http://notificacion-service/notificar/admin";

            try {
                restTemplate.postForEntity(notificacionUrl, body, Void.class);
            } catch (Exception e) {
                System.err.println("Error al notificar al admin por WhatsApp: " + e.getMessage());
            }
        } else {
            admin.setCodigoAutenticacion(null); // No se genera código si no es el primero
        }

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

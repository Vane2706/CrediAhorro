package upeu.edu.pe.auth_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.auth_service.entities.Admin;
import upeu.edu.pe.auth_service.service.AuthServiceImpl;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Operaciones de registro e inicio de sesión")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @Operation(summary = "Registrar un nuevo administrador")
    @PostMapping("/register")
    public ResponseEntity<Admin> register(@RequestBody Admin admin) {
        Admin nuevoAdmin = authService.register(admin);
        return ResponseEntity.ok(nuevoAdmin);
    }

    @Operation(summary = "Iniciar sesión con nombre de usuario, contraseña y código")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam String codigo) {
        boolean success = authService.login(username, password, codigo);
        return ResponseEntity.ok(success ? "Login successful" : "Invalid credentials or code");
    }
}

package upeu.edu.pe.auth_service.service;

import upeu.edu.pe.auth_service.entities.Admin;

public interface AuthService {

    Admin register(Admin admin);
    boolean login(String username, String password, String codigo);
}

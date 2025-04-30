package upeu.edu.pe.auth_service.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String telefonoWhatsapp;
    private String codigoAutenticacion;

    public Admin() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefonoWhatsapp() {
        return telefonoWhatsapp;
    }

    public void setTelefonoWhatsapp(String telefonoWhatsapp) {
        this.telefonoWhatsapp = telefonoWhatsapp;
    }

    public String getCodigoAutenticacion() {
        return codigoAutenticacion;
    }

    public void setCodigoAutenticacion(String codigoAutenticacion) {
        this.codigoAutenticacion = codigoAutenticacion;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", telefonoWhatsapp='" + telefonoWhatsapp + '\'' +
                ", codigoAutenticacion='" + codigoAutenticacion + '\'' +
                '}';
    }
}
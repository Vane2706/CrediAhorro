package upeu.edu.pe.admin_core_service.service;

import upeu.edu.pe.admin_core_service.entities.Cliente;
import upeu.edu.pe.admin_core_service.entities.Prestamo;

import java.util.Optional;

public interface ClienteService {
    Cliente guardarCliente(Cliente cliente);
    void generarCuotas(Prestamo prestamo);
    double calcularCuota(double monto, double tasa, int numeroCuotas);
    Optional<Cliente> obtenerClientePorId(Long id);
    Optional<Cliente> actualizarCliente(Long id, Cliente clienteActualizado);
    void eliminarCliente(Long id);
}
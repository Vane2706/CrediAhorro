package upeu.edu.pe.admin_core_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import upeu.edu.pe.admin_core_service.entities.Cliente;
import upeu.edu.pe.admin_core_service.entities.Cuota;
import upeu.edu.pe.admin_core_service.entities.Prestamo;
import upeu.edu.pe.admin_core_service.repository.ClienteRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService{

    private ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        cliente.getPrestamos().forEach(prestamo -> {
            prestamo.setEstado("ACTIVO");
            generarCuotas(prestamo);
        });
        return clienteRepository.save(cliente);
    }

    @Override
    public void generarCuotas(Prestamo prestamo) {
        double monto = prestamo.getMonto();
        double tasa = prestamo.getTasaInteresMensual() / 100; // convertir a decimal
        int numeroCuotas = prestamo.getNumeroCuotas();
        LocalDate fechaInicio = prestamo.getFechaInicio();

        double cuota = calcularCuota(monto, tasa, numeroCuotas);

        List<Cuota> cuotas = new ArrayList<>();
        for (int i = 0; i < numeroCuotas; i++) {
            Cuota nuevaCuota = new Cuota();
            nuevaCuota.setFechaPago(fechaInicio.plusMonths(i));
            nuevaCuota.setMontoCuota(cuota);
            nuevaCuota.setEstado("PENDIENTE");
            cuotas.add(nuevaCuota);
        }
        prestamo.setCuotas(cuotas);
    }

    @Override
    public double calcularCuota(double monto, double tasa, int n) {
        if (tasa == 0) {
            return Math.round((monto / n) * 100.0) / 100.0;
        }
        double cuota = monto * (tasa * Math.pow(1 + tasa, n)) / (Math.pow(1 + tasa, n) - 1);
        return Math.round(cuota * 100.0) / 100.0;
    }

    @Override
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Optional<Cliente> actualizarCliente(Long id, Cliente clienteActualizado) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setDni(clienteActualizado.getDni());
            cliente.setDireccion(clienteActualizado.getDireccion());
            cliente.setTelefonoWhatsapp(clienteActualizado.getTelefonoWhatsapp());
            cliente.setCorreoElectronico(clienteActualizado.getCorreoElectronico());
            return clienteRepository.save(cliente);
        });
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}

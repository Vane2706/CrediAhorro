package upeu.edu.pe.historial_service.repository;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import upeu.edu.pe.historial_service.beans.LoadBalancerConfiguration;
import upeu.edu.pe.historial_service.dto.Cuota;
import upeu.edu.pe.historial_service.dto.Prestamo;

import java.util.List;

@FeignClient(name = "admin-core-service")
@LoadBalancerClient(name = "admin-core-service", configuration = LoadBalancerConfiguration.class)
public interface AdminCoreClient {

    @GetMapping(path = "admin-core-service/consultas/prestamos")
    List<Prestamo> obtenerPrestamos(@RequestParam("clienteId") Long clienteId, @RequestParam("estado") String estado);

    @GetMapping(path = "admin-core-service/consultas/cuotas")
    List<Cuota> obtenerCuotas(@RequestParam("clienteId") Long clienteId, @RequestParam("estado") String estado);
}


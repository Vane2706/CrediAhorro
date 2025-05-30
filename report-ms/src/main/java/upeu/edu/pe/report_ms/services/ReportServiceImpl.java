package upeu.edu.pe.report_ms.services;

import org.springframework.stereotype.Service;
import upeu.edu.pe.report_ms.helpers.ReportHelper;
import upeu.edu.pe.report_ms.models.Prestamo;
import upeu.edu.pe.report_ms.models.Cuota;
import upeu.edu.pe.report_ms.repositories.ConsultaRepository;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ConsultaRepository consultaRepository;
    private final ReportHelper reportHelper;

    public ReportServiceImpl(ConsultaRepository consultaRepository, ReportHelper reportHelper) {
        this.consultaRepository = consultaRepository;
        this.reportHelper = reportHelper;
    }

    @Override
    public String generarReportePrestamos(Long clienteId, String estado) {
        List<Prestamo> prestamos = consultaRepository.obtenerPrestamosPorClienteYEstado(clienteId, estado);
        return reportHelper.formatearPrestamos(prestamos);
    }

    @Override
    public String generarReporteCuotas(Long clienteId, String estado) {
        List<Cuota> cuotas = consultaRepository.obtenerCuotasPorClienteYEstado(clienteId, estado);
        return reportHelper.formatearCuotas(cuotas);
    }
}

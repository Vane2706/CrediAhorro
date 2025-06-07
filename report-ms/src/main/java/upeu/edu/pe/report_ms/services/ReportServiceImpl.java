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
    public String generarReportePrestamos(String nombre, String estado) {
        List<Prestamo> prestamos = consultaRepository.obtenerPrestamosPorClienteYEstado(nombre, estado);
        return reportHelper.formatearPrestamos(prestamos);
    }

    @Override
    public String generarReporteCuotas(String nombre, String estado) {
        List<Cuota> cuotas = consultaRepository.obtenerCuotasPorClienteYEstado(nombre, estado);
        return reportHelper.formatearCuotas(cuotas);
    }
}
